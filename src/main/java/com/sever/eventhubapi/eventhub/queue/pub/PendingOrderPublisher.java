package com.sever.eventhubapi.eventhub.queue.pub;

import com.sever.eventhubapi.eventhub.config.RabbitMqProperties;
import com.sever.eventhubapi.eventhub.dto.OrderPaymentMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PendingOrderPublisher implements MessagePublisherInterface {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;

    @Override
    public void sendMessage(OrderPaymentMessageDto message) {
        log.info("PendingOrderPublisher.sendMessage:{}", message);
        rabbitTemplate.convertAndSend(rabbitMqProperties.topicExchangeNameMainExchange, rabbitMqProperties.routingKeyPendingMessages, message);
    }
}
