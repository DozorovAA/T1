package org.t1.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.t1.dto.UserDto;
import org.t1.kafka.KafkaUserIdProducer;
import org.t1.kafka.KafkaUserProducer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerKafkaConfig {
    @Value("${t1.kafka.topic.user_id_registered}")
    private String userTopic;
    @Value("${t1.kafka.bootstrap.server}")
    private String servers;

    private Map<String, Object> commonProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return props;
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable", havingValue = "true", matchIfMissing = true)
    public KafkaUserProducer producerUser(@Qualifier("user") KafkaTemplate<String, UserDto> template) {
        template.setDefaultTopic(userTopic);
        return new KafkaUserProducer(template);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable", havingValue = "true", matchIfMissing = true)
    public KafkaUserIdProducer producerUserId(@Qualifier("userId") KafkaTemplate<String, Long> template) {
        template.setDefaultTopic(userTopic);
        return new KafkaUserIdProducer(template);
    }

    @Bean("user")
    public KafkaTemplate<String, UserDto> usertKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(UserDto.class));
    }

    @Bean("userId")
    public KafkaTemplate<String, Long> userIdKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(Long.class));
    }

    private <T> ProducerFactory<String, T> producerFactory(Class<?> valueType) {
        Map<String, Object> props = commonProducerConfigs();
        if (valueType == Long.class) {
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        } else if (valueType == UserDto.class) {
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + valueType);
        }
        return new DefaultKafkaProducerFactory<>(props);
    }
}
