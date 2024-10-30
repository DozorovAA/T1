package org.t1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.t1.dto.UserDto;
import org.t1.kafka.KafkaUserProducer;
import org.t1.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @Value("${t1.kafka.topic.user_registration}")
    private String topic;


    @GetMapping(value = "/parse")
    public void parseSource() {
        List<UserDto> userDtos = userService.parseJson();
        userDtos.forEach(dto -> userService.sendTo(topic, dto));
    }
}
