package org.b8.kafka.connect.transforms.model;

import lombok.Builder;
import lombok.Data;
import org.apache.kafka.common.header.Header;

import java.util.List;

@Data
@Builder
public class BackupRecord {

    private String topic;
    private Integer partition;
    private Long offset;
    private Long timestamp;
    private Object key;
    private Object value;
    private List<Header> headers;

}
