package org.b8.kafka.connect.transforms.mapper;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.connect.header.Headers;

import java.util.ArrayList;
import java.util.List;

public class BackupHeadersMapper {

    public static List<Header> mapSinkHeaders(Headers headers) {
        var result = new ArrayList<Header>();
        if (headers != null) {
            headers.forEach(header -> result.add(BackupHeaderMapper.mapSinkHeader(header)));
        }
        return result;
    }
}
