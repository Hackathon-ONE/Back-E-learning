package learning.platform.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(

        @NotNull
        Long userId,

        @NotNull
        BigDecimal amount
) {
}
