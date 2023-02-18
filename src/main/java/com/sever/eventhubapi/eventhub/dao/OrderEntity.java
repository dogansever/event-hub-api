package com.sever.eventhubapi.eventhub.dao;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "T_ORDER")
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column
    private String detail;

    @Column(nullable = false)
    private BigDecimal totalPayment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column
    private LocalDateTime updateTime;

    @PrePersist
    void prePersist() {
        createTime = LocalDateTime.now();
        status = OrderStatus.PENDING;
    }

    @PreUpdate
    void preUpdate() {
        updateTime = LocalDateTime.now();
    }
}
