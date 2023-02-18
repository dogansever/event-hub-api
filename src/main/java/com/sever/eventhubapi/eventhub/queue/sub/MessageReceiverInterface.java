package com.sever.eventhubapi.eventhub.queue.sub;

import com.sever.eventhubapi.eventhub.dto.MessageDto;

public interface MessageReceiverInterface {

    void receiveMessage(MessageDto messageDto);
}
