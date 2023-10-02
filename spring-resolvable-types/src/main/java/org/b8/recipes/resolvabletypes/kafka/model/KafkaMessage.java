package org.b8.recipes.resolvabletypes.kafka.model;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.util.Map;

@Data
public class KafkaMessage<T> implements ResolvableTypeProvider {

    private T value;
    private Class type;
    private ConsumerRecord consumerRecord;
    private Map<String, Object> context;

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(value));
    }

}
