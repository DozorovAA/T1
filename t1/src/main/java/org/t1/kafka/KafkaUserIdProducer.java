package org.t1.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@ConditionalOnProperty(value = "t1.kafka.producer.enable", havingValue = "true", matchIfMissing = true)
@Component
public class KafkaUserIdProducer {
    @Value("${t1.kafka.topic.user_id_registered}")
    private String userTopic;
    private final KafkaTemplate<String, Long> template;

    public KafkaUserIdProducer(@Qualifier("userId") KafkaTemplate<String, Long> template) {
        this.template = template;
    }
    @PostConstruct
    protected void init(){
        template.setDefaultTopic(userTopic);
    }

    public void sendDefault(Long id) {
        try {
            log.info("send id {} in topic {}", id, template.getDefaultTopic());
            template.sendDefault(UUID.randomUUID().toString(), id).get();
            template.flush();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
