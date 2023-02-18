package com.sever.eventhubapi.eventhub.queue.pub;

import com.sever.eventhubapi.eventhub.dto.MessageDto;

public interface MessagePublisherInterface {
    void sendMessage(MessageDto message);
}
