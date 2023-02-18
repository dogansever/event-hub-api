package com.sever.eventhubapi.eventhub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqProperties {

    @Value("${custom.rabbitmq.topicExchangeName:main-exchange}")
    public String topicExchangeNameMainExchange;

    @Value("${custom.rabbitmq.queueName:pending-message-queue}")
    public String queueNamePendingMessages;

    @Value("${custom.rabbitmq.queueName2:approved-message-queue}")
    public String queueNameApprovedMessages;

    @Value("${custom.rabbitmq.routingKeyPattern:pending.#}")
    public String routingKeyPatternPendingMessages;

    @Value("${custom.rabbitmq.routingKeyPattern:approved.#}")
    public String routingKeyPatternApprovedMessages;

    @Value("${custom.rabbitmq.routingKey:pending.route1}")
    public String routingKeyPendingMessages;

    @Value("${custom.rabbitmq.routingKey:approved.route1}")
    public String routingKeyApprovedMessages;


}
