package com.sever.eventhubapi.eventhub.controller;

import com.sever.eventhubapi.eventhub.dto.MessageDto;
import com.sever.eventhubapi.eventhub.queue.pub.MessagePublisherInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private final MessagePublisherInterface pendingMessagePublisher;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated MessageDto dto) {
        log.info("MessageController create {}", dto);
        pendingMessagePublisher.sendMessage(dto);
    }
}
