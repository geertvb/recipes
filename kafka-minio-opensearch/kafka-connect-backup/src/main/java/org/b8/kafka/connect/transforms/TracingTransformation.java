package org.b8.kafka.connect.transforms;

import brave.Clock;
import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.internal.codec.HexCodec;
import brave.propagation.TraceContextOrSamplingFlags;
import brave.propagation.TraceIdContext;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.brave.ZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class TracingTransformation implements Transformation<SinkRecord> {

    protected OkHttpSender sender;
    protected ZipkinSpanHandler zipkinSpanHandler;
    protected Map<String, Tracing> tracingCache = new HashMap<>();
    protected Map<String, Tracer> tracerCache = new HashMap<>();

    protected String getHeader(SinkRecord sinkRecord, String key) {
        return "" + sinkRecord
                    .headers()
                    .lastWithName(key)
                    .value();
    }

    @Override
    public SinkRecord apply(SinkRecord sinkRecord) {
        var traceId = getHeader(sinkRecord, "X-B3-TraceId");
        var application = getHeader(sinkRecord, "application");
        var typeId = getHeader(sinkRecord, "__typeId__");

        TraceIdContext traceIdContext = TraceIdContext.newBuilder()
                    .traceId(HexCodec.lowerHexToUnsignedLong(traceId))
                    .sampled(true)
                    .build();

        TraceContextOrSamplingFlags extracted = TraceContextOrSamplingFlags.newBuilder(traceIdContext).build();
        Span span = getTracer(application)
                    .nextSpan(extracted)
                    .name(typeId + "(" + sinkRecord.key() + ")")
                    .remoteServiceName(application)
                    .start();
        try {
            span.tag("topic", sinkRecord.topic());
            span.tag("partition", "" + sinkRecord.kafkaPartition());
            span.tag("offset", "" + sinkRecord.kafkaOffset());
            span.tag("timestamp", "" + Instant.ofEpochMilli(sinkRecord.timestamp()));
            return sinkRecord;
        } catch (RuntimeException | Error e) {
            span.error(e); // Unless you handle exceptions, you might not know the operation failed!
            throw e;
        } finally {
            Clock clock = getTracing(application).clock(span.context());
            long finishMicros = clock.currentTimeMicroseconds() + 100000;
            span.finish(finishMicros); // always finish the span
        }
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef();
    }

    @Override
    public void close() {
        tracingCache.values().forEach(Tracing::close);
        zipkinSpanHandler.close();
        sender.close();
    }

    protected Tracing getTracing(String serviceName) {
        return tracingCache.computeIfAbsent(serviceName, name -> Tracing.newBuilder()
                    .localServiceName("KC_" + name)
                    .addSpanHandler(zipkinSpanHandler)
                    .build());
    }

    protected Tracer getTracer(String serviceName) {
        return tracerCache.computeIfAbsent(serviceName, name -> getTracing(name)
                    .tracer());
    }

    @Override
    public void configure(Map<String, ?> map) {
        var sender = OkHttpSender.create("http://zipkin:9411/api/v2/spans");
        zipkinSpanHandler = AsyncZipkinSpanHandler.create(sender);
    }

}
