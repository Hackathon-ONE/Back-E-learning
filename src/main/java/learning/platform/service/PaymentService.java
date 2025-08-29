package learning.platform.service;

import learning.platform.dto.PaymentRequest;
import learning.platform.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse pay(PaymentRequest paymentRequest, Long userId);
    PaymentResponse updatePayment(Long userId);
    PaymentResponse getPayments(Long userId);
    PaymentResponse cancelTransaction(Long UserId);
}
