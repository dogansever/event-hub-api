package com.sever.eventhubapi.eventhub.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final RabbitMqProperties rabbitMqProperties;

    @Bean
    Queue queuePendingMessages() {
        return new Queue(rabbitMqProperties.queueNamePendingMessages, false);
    }

    @Bean
    Queue queueApprovedMessages() {
        return new Queue(rabbitMqProperties.queueNameApprovedMessages, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(rabbitMqProperties.topicExchangeNameMainExchange);
    }

    @Bean
    Binding bindingPendingMessages(Queue queuePendingMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queuePendingMessages).to(exchange).with(rabbitMqProperties.routingKeyPatternPendingMessages);
    }

    @Bean
    Binding bindingApprovedMessages(Queue queueApprovedMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueApprovedMessages).to(exchange).with(rabbitMqProperties.routingKeyPatternApprovedMessages);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }
}
