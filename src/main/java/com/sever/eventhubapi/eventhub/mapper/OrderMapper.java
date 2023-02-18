package com.sever.eventhubapi.eventhub.mapper;

import com.sever.eventhubapi.eventhub.dao.OrderEntity;
import com.sever.eventhubapi.eventhub.dao.PaymentEntity;
import com.sever.eventhubapi.eventhub.dto.OrderDto;
import com.sever.eventhubapi.eventhub.dto.OrderPaymentMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);
    @Mapping(target="id", ignore = true)
    @Mapping(target="status", ignore = true)
    OrderEntity toEntity(OrderDto dto);

    @Mapping(target="orderId", source = "id")
    @Mapping(target="orderStatus", source = "status")
    OrderPaymentMessageDto toDto(OrderEntity entity);

    @Mapping(target="paymentStatus", source = "status")
    OrderPaymentMessageDto toDto(PaymentEntity entity);
}
