package com.sever.eventhubapi.eventhub.controller;

import com.sever.eventhubapi.eventhub.dao.OrderEntity;
import com.sever.eventhubapi.eventhub.dao.OrderRepository;
import com.sever.eventhubapi.eventhub.dao.OrderStatus;
import com.sever.eventhubapi.eventhub.dto.OrderDto;
import com.sever.eventhubapi.eventhub.mapper.OrderMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("")
    public List<OrderDto> getAll() {
        return orderMapper.toDto(orderRepository.findAll(Example.of(OrderEntity.builder().status(OrderStatus.PENDING).build()), Sort.by(Sort.Direction.DESC, "createTime")));
    }
}
