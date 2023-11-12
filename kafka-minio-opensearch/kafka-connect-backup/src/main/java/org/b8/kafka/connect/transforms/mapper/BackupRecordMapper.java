package org.b8.kafka.connect.transforms.mapper;

import org.apache.kafka.connect.sink.SinkRecord;
import org.b8.kafka.connect.transforms.model.BackupRecord;

import static org.b8.kafka.connect.transforms.mapper.BackupHeadersMapper.mapSinkHeaders;

public class BackupRecordMapper {
    public static BackupRecord mapSinkRecord(SinkRecord sinkRecord) {
        return BackupRecord.builder()
                .topic(sinkRecord.topic())
                .partition(sinkRecord.kafkaPartition())
                .offset(sinkRecord.kafkaOffset())
                .timestamp(sinkRecord.timestamp())
                .key(sinkRecord.key())
                .value(sinkRecord.value())
                .headers(mapSinkHeaders(sinkRecord.headers()))
                .build();
    }
}
