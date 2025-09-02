package learning.platform.dto;

import learning.platform.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long userId,
        BigDecimal amount,
        LocalDateTime paidAt,
        LocalDateTime expiresAt
) {
    public PaymentResponse(Payment payment) {
        this(payment.getId(), payment.getUser().getId(),
                payment.getAmount(), payment.getPaidAt(), payment.getExpiresAt());
    }
}
