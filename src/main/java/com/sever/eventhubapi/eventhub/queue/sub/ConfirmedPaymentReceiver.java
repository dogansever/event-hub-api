package com.sever.eventhubapi.eventhub.queue.sub;

import com.sever.eventhubapi.eventhub.dao.OrderEntity;
import com.sever.eventhubapi.eventhub.dao.OrderRepository;
import com.sever.eventhubapi.eventhub.dao.OrderStatus;
import com.sever.eventhubapi.eventhub.dao.PaymentStatus;
import com.sever.eventhubapi.eventhub.dto.OrderPaymentMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmedPaymentReceiver implements MessageReceiverInterface {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = {"${custom.rabbitmq.queueName:approved-payment-queue}"})
    @Override
    public void receiveMessage(OrderPaymentMessageDto message) {
        log.info("ConfirmedPaymentReceiver.receiveMessage:{}", message);
        if (PaymentStatus.valueOf(message.getPaymentStatus()).equals(PaymentStatus.CONFIRMED)) {
            Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(message.getOrderId());
            OrderEntity orderEntity = optionalOrderEntity.get();
            if (orderEntity.getStatus().equals(OrderStatus.PAID)) {
                log.warn("OrderEntity={} status is already PAID", orderEntity);
            } else {
                orderEntity.setStatus(OrderStatus.PAID);
                orderRepository.save(orderEntity);
                log.info("Order is marked as PAID:{}", orderEntity.getTotalPayment());
            }
        }
        else if (PaymentStatus.valueOf(message.getPaymentStatus()).equals(PaymentStatus.FAILED)) {
            Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(message.getOrderId());
            OrderEntity orderEntity = optionalOrderEntity.get();
            if (orderEntity.getStatus().equals(OrderStatus.PENDING)) {
                orderEntity.setStatus(OrderStatus.PAYMENT_ERROR);
                orderRepository.save(orderEntity);
                log.warn("OrderEntity={}", orderEntity);
            }
        }
    }
}
