package com.sever.eventhubapi.eventhub.config;

import com.sever.eventhubapi.eventhub.dao.*;
import com.sever.eventhubapi.eventhub.exception.AssertionUtil;
import com.sever.eventhubapi.eventhub.exception.BaseException;
import com.sever.eventhubapi.eventhub.exception.PaymentDeclinedException;
import com.sever.eventhubapi.eventhub.exception.TooManyAttemptException;
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

    private final MessagePublisherInterface pendingOrderPublisher;
    private final MessagePublisherInterface confirmedPaymentPublisher;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;

    @Scheduled(fixedDelay = 10000)
    public void processPendingOrders() {
        OrderEntity entity = OrderEntity.builder().status(OrderStatus.PENDING).build();
        List<OrderEntity> entityList = orderRepository.findAll(Example.of(entity));
        entityList.stream().forEach(e -> {
            pendingOrderPublisher.sendMessage(orderMapper.toOrderPaymentMessageDto(e));
        });
    }

    @Scheduled(fixedDelay = 15000)
    public void processWaitingPayments() {
        PaymentEntity entity = new PaymentEntity();
        entity.setStatus(PaymentStatus.WAITING);
        List<PaymentEntity> entityList = paymentRepository.findAll(Example.of(entity));
        entityList.stream().forEach(this::chargePayment);
    }

    private void chargePayment(PaymentEntity paymentEntity) {
        try {
            AssertionUtil.assertTrue(paymentEntity.updateChargeAttemptCount() < 3, "Too many charge attempt", TooManyAttemptException.class);
            paymentRepository.save(paymentEntity);

            AssertionUtil.assertTrue(paymentEntity.getTotalPayment().longValue() < 500, "Declined Payment");

            log.info("Payment charged {}", paymentEntity.getTotalPayment());
            paymentEntity.setStatus(PaymentStatus.CONFIRMED);
            paymentRepository.save(paymentEntity);

            AssertionUtil.failRandom();
            confirmedPaymentPublisher.sendMessage(orderMapper.toOrderPaymentMessageDto(paymentEntity));
        } catch (PaymentDeclinedException e) {
            log.error("PaymentDeclinedException:{}", e.getMessage());
        } catch (TooManyAttemptException e) {
            log.error("TooManyAttemptException:{}", e.getMessage());
            paymentEntity.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(paymentEntity);
            confirmedPaymentPublisher.sendMessage(orderMapper.toOrderPaymentMessageDto(paymentEntity));
        } catch (BaseException e) {
            log.error("BaseException:{}", e.getMessage());
        }
    }
}
