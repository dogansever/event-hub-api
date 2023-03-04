package com.sever.eventhubapi.eventhub.dao.entity;

import com.sever.eventhubapi.eventhub.dao.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Builder
@Entity
@Table(name = "T_ORDER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity implements Serializable {

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
