package com.sever.eventhubapi.eventhub.config;

import com.sever.eventhubapi.eventhub.dao.*;
import com.sever.eventhubapi.eventhub.mapper.OrderMapper;
import com.sever.eventhubapi.eventhub.queue.pub.MessagePublisherInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final MessagePublisherInterface pendingMessagePublisher;
    private final MessagePublisherInterface approvedMessagePublisher;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;

    @Scheduled(fixedDelay = 10000)
    public void processPendingOrders() {
        OrderEntity entity = new OrderEntity();
        entity.setStatus(OrderStatus.PENDING);
        List<OrderEntity> entityList = orderRepository.findAll(Example.of(entity));
        entityList.stream().forEach(e->{pendingMessagePublisher.sendMessage(orderMapper.toDto(e));});
    }

    @Scheduled(fixedDelay = 15000)
    public void processPendingPayments() {
        PaymentEntity entity = new PaymentEntity();
        entity.setStatus(PaymentStatus.WAITING);
        List<PaymentEntity> entityList = paymentRepository.findAll(Example.of(entity));
        for (PaymentEntity paymentEntity : entityList) {
            paymentEntity.setStatus(PaymentStatus.CONFIRMED);
            paymentRepository.save(paymentEntity);
            log.info("Payment charged {}", paymentEntity.getTotalPayment());
        }

        entityList.stream().forEach(e->{approvedMessagePublisher.sendMessage(orderMapper.toDto(e));});
    }
}
