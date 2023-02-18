package com.sever.eventhubapi.eventhub.controller;

import com.sever.eventhubapi.eventhub.dao.OrderRepository;
import com.sever.eventhubapi.eventhub.dto.OrderDto;
import com.sever.eventhubapi.eventhub.mapper.OrderMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Validated OrderDto dto) {
        log.info("OrderController create {}", dto);
        orderRepository.save(orderMapper.toEntity(dto));
    }
}
