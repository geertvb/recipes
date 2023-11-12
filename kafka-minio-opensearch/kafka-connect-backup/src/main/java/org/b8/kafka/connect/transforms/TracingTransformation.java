package org.b8.kafka.connect.transforms;

import brave.ScopedSpan;
import brave.Tracer;
import brave.Tracing;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.brave.ZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.time.Instant;
import java.util.Map;

public class TracingTransformation implements Transformation<SinkRecord> {

    protected OkHttpSender sender;
    protected ZipkinSpanHandler zipkinSpanHandler;
    protected Tracing tracing;
    protected Tracer tracer;

    @Override
    public SinkRecord apply(SinkRecord sinkRecord) {
        ScopedSpan span = tracer.startScopedSpan("trace-connect");
        try {
            span.tag("topic", sinkRecord.topic());
            span.tag("partition", "" + sinkRecord.kafkaPartition());
            span.tag("offset", "" + sinkRecord.kafkaOffset());
            span.tag("timestamp", "" + Instant.ofEpochMilli(sinkRecord.timestamp()));
            // TODO: headers, message type, ...
            return sinkRecord;
        } catch (RuntimeException | Error e) {
            span.error(e); // Unless you handle exceptions, you might not know the operation failed!
            throw e;
        } finally {
            span.finish(); // always finish the span
        }
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef();
    }

    @Override
    public void close() {
        tracing.close();
        zipkinSpanHandler.close();
        sender.close();
    }

    @Override
    public void configure(Map<String, ?> map) {
        var sender = OkHttpSender.create("http://zipkin:9411/api/v2/spans");
        zipkinSpanHandler = AsyncZipkinSpanHandler.create(sender);
        tracing = Tracing.newBuilder()
                .localServiceName("kafka-connect")
                .addSpanHandler(zipkinSpanHandler)
                .build();
        tracer = tracing.tracer();
    }

}
