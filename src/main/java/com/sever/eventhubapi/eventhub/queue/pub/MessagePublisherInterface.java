package com.sever.eventhubapi.eventhub.queue.pub;

import com.sever.eventhubapi.eventhub.dto.OrderPaymentMessageDto;

public interface MessagePublisherInterface {
    void sendMessage(OrderPaymentMessageDto message);
}
