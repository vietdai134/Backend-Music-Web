package com.app.Music_Web.Application.DTO;

import java.util.Date;

import com.app.Music_Web.Domain.Enums.PaymentStatus;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentDTO {
    private Long paymentId;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private Date paymentDate;
    private Date paymentExpiry;
    private String userName;
}
