package com.sever.eventhubapi.eventhub.dao;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "T_PAYMENT")
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column
    private Integer chargeAttemptCount;

    @Column
    private BigDecimal totalPayment;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column
    private LocalDateTime updateTime;

    @PrePersist
    void prePersist() {
        chargeAttemptCount = 0;
        createTime = LocalDateTime.now();
        status = PaymentStatus.WAITING;
    }

    @PreUpdate
    void preUpdate() {
        updateTime = LocalDateTime.now();
    }

    public int updateChargeAttemptCount() {
        chargeAttemptCount++;
        return chargeAttemptCount;
    }
}
