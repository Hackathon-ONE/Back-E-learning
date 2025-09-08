package learning.platform.service;

import learning.platform.dto.PaymentRequest;
import learning.platform.dto.PaymentResponse;
import java.util.List;

public interface PaymentService {
    PaymentResponse pay(PaymentRequest paymentRequest);
    PaymentResponse updatePayment(Long userId);
    PaymentResponse getPayments(Long userId);
    PaymentResponse cancelTransaction(Long UserId);
    List<PaymentResponse> findAll();
}
