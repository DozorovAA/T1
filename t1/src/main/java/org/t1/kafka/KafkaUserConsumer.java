package org.t1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.t1.dto.UserDto;
import org.t1.entity.User;
import org.t1.service.UserService;
import org.t1.util.UserMapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaUserConsumer {

    private final UserService userService;

    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = "${t1.kafka.topic.user_registration}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<UserDto> messageList, Acknowledgment ack) {
        try {
            List<User> users = messageList.stream()
                    .map(UserMapper::toEntity)
                    .toList();

            userService.registerUsers(users);
        } finally {
            ack.acknowledge();
        }

        log.debug("User consumer: записи обработаны");
    }
}
