package org.b8.kafka.connect.transforms.mapper;

import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.connect.header.Header;

public class BackupHeaderMapper {

    public static byte[] mapValue(Object value) {
        return null;
    }

    public static RecordHeader mapSinkHeader(Header sinkHeader) {
        return new RecordHeader(
                sinkHeader.key(),
                mapValue(sinkHeader.value()));
    }

}
