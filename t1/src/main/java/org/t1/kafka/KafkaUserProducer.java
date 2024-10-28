package org.t1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.t1.dto.UserDto;

@Slf4j
@RequiredArgsConstructor
public class KafkaUserProducer {

    private final KafkaTemplate<String, UserDto> template;

    public void sendTo(String topic, UserDto userDto) {
        try {
            template.send(topic, userDto.getFirstName(), userDto).get();
            template.flush();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}

