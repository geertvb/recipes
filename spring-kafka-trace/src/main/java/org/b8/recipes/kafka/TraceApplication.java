package org.b8.recipes.kafka;

import brave.Tracing;
import brave.TracingCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import java.util.UUID;
import java.util.function.Supplier;

@SpringBootApplication
public class TraceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TraceApplication.class, args);
    }

    @Bean
    public Supplier<String> idSupplier() {
        return () -> UUID.randomUUID().toString();
    }

    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate(
            KafkaProperties properties,
            ProducerFactory<Object, Object> kafkaProducerFactory,
            ProducerListener<Object, Object> kafkaProducerListener,
            ObjectProvider<RecordMessageConverter> messageConverter) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        map.from(kafkaProducerListener).to(kafkaTemplate::setProducerListener);
        map.from(properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
        map.from(properties.getTemplate().getTransactionIdPrefix()).to(kafkaTemplate::setTransactionIdPrefix);

        // Required for trace id propagation
        kafkaTemplate.setObservationEnabled(true);

        return kafkaTemplate;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            KafkaProperties properties,
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory,
            ObjectProvider<ContainerCustomizer<Object, Object, ConcurrentMessageListenerContainer<Object, Object>>> kafkaContainerCustomizer) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory
                .getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties())));
        kafkaContainerCustomizer.ifAvailable(factory::setContainerCustomizer);

        // Required for trace id propagation
        factory.getContainerProperties().setObservationEnabled(true);

        return factory;
    }

    @Bean
    public TracingCustomizer brave64bitCustomizer() {

        // Required for 64bit backward compatibility
        return (Tracing.Builder builder) -> builder.traceId128Bit(false);
    }

}
