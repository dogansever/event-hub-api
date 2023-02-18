package com.sever.eventhubapi.eventhub.queue.sub;

import com.sever.eventhubapi.eventhub.dto.MessageDto;
import com.sever.eventhubapi.eventhub.exception.AssertionUtil;
import com.sever.eventhubapi.eventhub.queue.pub.MessagePublisherInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovedMessageReceiver implements MessageReceiverInterface {


    private final MessagePublisherInterface pendingMessagePublisher;

    @RabbitListener(queues = {"${custom.rabbitmq.queueName:approved-message-queue}"})
    @Override
    public void receiveMessage(MessageDto messageDto) {
        AssertionUtil.fail();
        log.info("ApprovedMessageReceiver receiveMessage {}", messageDto);
    }
}
