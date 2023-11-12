package org.b8.kafka.connect.transforms;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class IndexTransformation implements Transformation<SinkRecord> {

    @Override
    public SinkRecord apply(SinkRecord sinkRecord) {
        var newValue = new HashMap<>();
        newValue.put("topic", sinkRecord.topic());
        newValue.put("partition", sinkRecord.kafkaPartition());
        newValue.put("offset", sinkRecord.kafkaOffset());
        newValue.put("@timestamp", "" + Instant.ofEpochMilli(sinkRecord.timestamp()));
        newValue.put("timestamp", sinkRecord.timestamp());
        newValue.put("key", "" + sinkRecord.key());
        newValue.put("value", "" + sinkRecord.value());

        return new SinkRecord(
                sinkRecord.topic(),
                sinkRecord.kafkaPartition(),
                sinkRecord.keySchema(),
                sinkRecord.key(),
                sinkRecord.valueSchema(),
                newValue,
                sinkRecord.kafkaOffset(),
                sinkRecord.timestamp(),
                sinkRecord.timestampType(),
                sinkRecord.headers());
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef();
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }

}
