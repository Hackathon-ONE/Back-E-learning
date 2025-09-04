package learning.platform.mapper;

import learning.platform.dto.PaymentResponse;
import learning.platform.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentResponse toResponce(Payment payment){return new PaymentResponse(payment);}
}
