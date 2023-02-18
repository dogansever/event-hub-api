package com.sever.eventhubapi.eventhub.queue.sub;

import com.sever.eventhubapi.eventhub.dao.OrderEntity;
import com.sever.eventhubapi.eventhub.dao.OrderRepository;
import com.sever.eventhubapi.eventhub.dao.OrderStatus;
import com.sever.eventhubapi.eventhub.dto.OrderPaymentMessageDto;
import com.sever.eventhubapi.eventhub.queue.pub.MessagePublisherInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovedMessageReceiver implements MessageReceiverInterface {


    private final MessagePublisherInterface pendingMessagePublisher;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = {"${custom.rabbitmq.queueName:approved-message-queue}"})
    @Override
    public void receiveMessage(OrderPaymentMessageDto message) {
        log.info("ApprovedMessageReceiver receiveMessage {}", message);
        if (message.getPaymentStatus().equals("CONFIRMED")) {
            Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(message.getOrderId());
            OrderEntity orderEntity = optionalOrderEntity.get();
            if (orderEntity.getStatus().equals(OrderStatus.PAID)) {
                log.warn("OrderEntity={} status is already PAID", orderEntity);
            } else {
                orderEntity.setStatus(OrderStatus.PAID);
                orderRepository.save(orderEntity);
                log.info("Order is marked as PAID, {}", orderEntity.getTotalPayment());
            }
        }
    }
}
