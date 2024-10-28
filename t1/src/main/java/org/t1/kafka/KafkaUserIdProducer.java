package org.t1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class KafkaUserIdProducer {
    private final KafkaTemplate<String, Long> template;

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
