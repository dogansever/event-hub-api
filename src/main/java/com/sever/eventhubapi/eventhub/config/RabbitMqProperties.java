package com.sever.eventhubapi.eventhub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqProperties {

    @Value("${custom.rabbitmq.topicExchangeName:main-exchange}")
    public String topicExchangeNameMainExchange;

    @Value("${custom.rabbitmq.queueNameForPendingOrder:pending-order-queue}")
    public String queueNamePendingMessages;

    @Value("${custom.rabbitmq.queueNameApprovedPayment:approved-payment-queue}")
    public String queueNameApprovedMessages;

    @Value("${custom.rabbitmq.routingKeyPatternForPendingOrder:pending.#}")
    public String routingKeyPatternPendingMessages;

    @Value("${custom.rabbitmq.routingKeyPatternForApprovedPayment:approved.#}")
    public String routingKeyPatternApprovedMessages;

    @Value("${custom.rabbitmq.routingKeyForPendingOrder:pending.route}")
    public String routingKeyPendingMessages;

    @Value("${custom.rabbitmq.routingKeyForApprovedPayment:approved.route}")
    public String routingKeyApprovedMessages;


}
