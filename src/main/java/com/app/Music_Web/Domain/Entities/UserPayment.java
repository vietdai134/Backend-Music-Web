package com.app.Music_Web.Domain.Entities;

import java.util.Date;

import com.app.Music_Web.Domain.Enums.PaymentMethod;
import com.app.Music_Web.Domain.Enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_payment")
public class UserPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(nullable = false,name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "payment_status")
    private PaymentStatus paymentStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false,name = "payment_date")
    private Date paymentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false,name = "payment_expiry")
    private Date paymentExpiry;

    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

}
