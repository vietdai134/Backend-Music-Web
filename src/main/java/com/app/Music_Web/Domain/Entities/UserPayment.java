package com.app.Music_Web.Domain.Entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "user_payment")
public class UserPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(nullable = false,name = "payment_method")
    private String paymentMethod;

    @Column(nullable = false,name = "payment_status")
    private String paymentStatus;

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
