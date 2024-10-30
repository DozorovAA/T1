package org.t1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.t1.dto.UserDto;
import org.t1.entity.User;
import org.t1.kafka.KafkaUserIdProducer;
import org.t1.kafka.KafkaUserProducer;
import org.t1.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final KafkaUserIdProducer kafkaUserIdProducer;
    private final KafkaUserProducer kafkaUserProducer;
    private final UserRepository repository;

    public void registerUsers(List<User> users) {
        log.info("Registering users... {}", users);
        repository.saveAll(users).stream()
                .map(User::getId)
                .forEach(kafkaUserIdProducer::sendDefault);
    }


    public List<UserDto> parseJson() {
        ObjectMapper mapper = new ObjectMapper();

        UserDto[] userDtos;
        try {
            userDtos = mapper.readValue(new File("src/main/resources/MOCK_DATA.json"), UserDto[].class);
        } catch (IOException e) {
            log.warn("Ошибка парсинга json ", e);
            throw new RuntimeException(e);
        }

        return Arrays.asList(userDtos);
    }


    public void sendTo(String topic, UserDto dto) {
        kafkaUserProducer.sendTo(topic, dto);
    }
}
