package com.sever.eventhubapi.eventhub.queue.sub;

import com.sever.eventhubapi.eventhub.dao.PaymentEntity;
import com.sever.eventhubapi.eventhub.dao.PaymentRepository;
import com.sever.eventhubapi.eventhub.dao.PaymentStatus;
import com.sever.eventhubapi.eventhub.dto.OrderPaymentMessageDto;
import com.sever.eventhubapi.eventhub.mapper.OrderMapper;
import com.sever.eventhubapi.eventhub.queue.pub.MessagePublisherInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PendingOrderReceiver implements MessageReceiverInterface {

    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;
    private final MessagePublisherInterface confirmedPaymentPublisher;

    @RabbitListener(queues = {"${custom.rabbitmq.queueName:pending-message-queue}"})
    @Override
    public void receiveMessage(OrderPaymentMessageDto message) {
        log.info("PendingOrderReceiver.receiveMessage:{}", message);
        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(message.getOrderId());
        List<PaymentEntity> paymentEntityList = paymentRepository.findAll(Example.of(payment));
        if (paymentEntityList.size() != 0) {
            PaymentEntity paymentEntity = paymentEntityList.get(0);
            log.warn("Message received for already existing PaymentEntity={} for orderId={}", paymentEntity, message.getOrderId());
            if (paymentEntity.getStatus().equals(PaymentStatus.CONFIRMED)) {
                confirmedPaymentPublisher.sendMessage(orderMapper.toOrderPaymentMessageDto(paymentEntity));
            }
        } else {
            PaymentEntity paymentEntity = new PaymentEntity();
            paymentEntity.setOrderId(message.getOrderId());
            paymentEntity.setTotalPayment(message.getTotalPayment());
            paymentRepository.save(paymentEntity);
            log.info("Saving new PaymentEntity={} for orderId={}", paymentEntity, message.getOrderId());
        }
    }
}
