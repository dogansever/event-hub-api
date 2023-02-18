package com.sever.eventhubapi.eventhub.queue.sub;

import com.sever.eventhubapi.eventhub.dto.MessageDto;
import com.sever.eventhubapi.eventhub.queue.pub.MessagePublisherInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class PendingMessageReceiver implements MessageReceiverInterface {


    private final MessagePublisherInterface approvedMessagePublisher;

    @RabbitListener(queues = {"${custom.rabbitmq.queueName:pending-message-queue}"})
    @Override
    public void receiveMessage(MessageDto messageDto) {
        log.info("PendingMessageReceiver receiveMessage {}", messageDto);

        Integer messageNo = messageDto.getMessageNo();
        String prefixMessage = messageDto.getMessage().substring(0, messageNo % messageDto.getMessage().length() + 1).toUpperCase(Locale.ROOT);
        String suffixMessage = messageDto.getMessage().substring(messageNo % messageDto.getMessage().length() + 1).toLowerCase(Locale.ROOT);
        String newMessage = prefixMessage + suffixMessage;

        messageDto = MessageDto.builder().message(newMessage).messageNo(messageNo + 1).status("Approved").build();
        approvedMessagePublisher.sendMessage(messageDto);
    }
}
