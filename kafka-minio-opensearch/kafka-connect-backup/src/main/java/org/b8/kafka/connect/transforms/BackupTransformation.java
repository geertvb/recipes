package org.b8.kafka.connect.transforms;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.Map;

import static org.b8.kafka.connect.transforms.mapper.BackupRecordMapper.mapSinkRecord;

public class BackupTransformation implements Transformation<SinkRecord> {

    @Override
    public SinkRecord apply(SinkRecord sinkRecord) {
        var newValue = mapSinkRecord(sinkRecord);

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
