package com.app.Music_Web.Domain.Enums;

public enum PaymentStatus {
    PENDING,    // Đang chờ xử lý
    COMPLETED,  // Thanh toán thành công
    FAILED,     // Thanh toán thất bại
    CANCELED,   // Đã hủy
    REFUNDED    // Đã hoàn tiền
}
