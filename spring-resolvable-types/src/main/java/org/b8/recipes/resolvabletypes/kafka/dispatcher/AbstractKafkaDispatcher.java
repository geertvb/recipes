package org.b8.recipes.resolvabletypes.kafka.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.b8.recipes.resolvabletypes.kafka.model.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@Slf4j
public abstract class AbstractKafkaDispatcher {

    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    protected abstract List<Class> getSupportedTypes();

    protected abstract KafkaMessage createKafkaMessage();

    public void commandHandler(ConsumerRecord<String, Object> consumerRecord) {
        log.debug("Handle consumer record: {}", consumerRecord);

        try {
            for (Class supportedType : getSupportedTypes()) {
                Object value = consumerRecord.value();
                if (supportedType.isInstance(value)) {
                    log.info("Handle supported type: {}", supportedType);

                    KafkaMessage kafkaMessage = createKafkaMessage();
                    kafkaMessage.setConsumerRecord(consumerRecord);
                    kafkaMessage.setValue(value);
                    kafkaMessage.setType(supportedType);

                    applicationEventPublisher.publishEvent(kafkaMessage);
                }
            }
        } catch (Exception e) {
            log.error("Failed to handle consumer record: {}", consumerRecord, e);
        }
    }

}
