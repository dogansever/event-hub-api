package com.sever.eventhubapi.eventhub.mapper;

import com.sever.eventhubapi.eventhub.dao.OrderEntity;
import com.sever.eventhubapi.eventhub.dao.PaymentEntity;
import com.sever.eventhubapi.eventhub.dto.OrderDto;
import com.sever.eventhubapi.eventhub.dto.OrderPaymentMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);
    @Mapping(target="id", ignore = true)
    @Mapping(target="status", ignore = true)
    OrderEntity toEntity(OrderDto dto);

    @Mapping(target="orderId", source = "id")
    @Mapping(target="orderStatus", source = "status")
    OrderPaymentMessageDto toOrderPaymentMessageDto(OrderEntity entity);

    @Mapping(target="paymentStatus", source = "status")
    OrderPaymentMessageDto toOrderPaymentMessageDto(PaymentEntity entity);

    OrderDto toDto(OrderEntity entity);
    List<OrderDto> toDto(List<OrderEntity> entityList);
}
