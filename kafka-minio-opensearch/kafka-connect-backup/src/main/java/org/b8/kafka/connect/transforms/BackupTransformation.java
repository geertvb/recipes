package org.b8.kafka.connect.transforms;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class BackupTransformation implements Transformation<SinkRecord> {

    @Override
    public SinkRecord apply(SinkRecord sinkRecord) {
        var newValue = new LinkedHashMap<>();
        newValue.put("topic", sinkRecord.topic());
        newValue.put("partition", sinkRecord.kafkaPartition());
        newValue.put("offset", sinkRecord.kafkaOffset());
        newValue.put("timestamp", sinkRecord.timestamp());
        newValue.put("key", sinkRecord.key());
        newValue.put("value", sinkRecord.value());
        ArrayList headerList = new ArrayList();
        if (sinkRecord.headers() != null) {
            sinkRecord.headers().forEach(header ->
                    headerList.add(Map.of(
                            "key", header.key(),
                            "value", header.value())));
        }
        newValue.put("headers", headerList);

        return new SinkRecord(
                sinkRecord.topic(),
                sinkRecord.kafkaPartition(),
                sinkRecord.keySchema(),
                sinkRecord.key(),
                null,
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
