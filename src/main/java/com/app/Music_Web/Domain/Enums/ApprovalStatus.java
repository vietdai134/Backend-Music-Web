package com.app.Music_Web.Domain.Enums;

public enum ApprovalStatus {
    PENDING,       // Đang chờ duyệt
    APPROVED,      // Đã được duyệt
    REJECTED,      // Bị từ chối
    UNDER_REVIEW,  // Đang xem xét lại
    REVOKED        // Bị thu hồi phê duyệt
}
