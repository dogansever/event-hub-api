package com.sever.eventhubapi.eventhub.queue.sub;

import com.sever.eventhubapi.eventhub.dto.OrderPaymentMessageDto;

public interface MessageReceiverInterface {

    void receiveMessage(OrderPaymentMessageDto paymentDto);
}
