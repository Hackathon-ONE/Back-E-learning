package learning.platform.service.impl;

import jakarta.transaction.Transactional;
import learning.platform.dto.PaymentRequest;
import learning.platform.dto.PaymentResponse;
import learning.platform.entity.Payment;
import learning.platform.exception.ResourceNotFoundException;
import learning.platform.mapper.PaymentMapper;
import learning.platform.repository.PaymentRepository;
import learning.platform.repository.UserRepository;
import learning.platform.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final UserRepository userRepository;
    private final PaymentMapper mapper;

    public PaymentServiceImpl(PaymentRepository repository, UserRepository userRepository, PaymentMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public PaymentResponse pay(PaymentRequest paymentRequest) {
        var user = userRepository.findById(paymentRequest.userId()).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        var payment = new Payment(paymentRequest,user);
        user.setSubscribed(true);
        userRepository.save(user);
        repository.save(payment);
        return new PaymentResponse(payment);
    }

    @Transactional
    @Override
    public PaymentResponse updatePayment(Long userId) {
        var payment = repository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        payment.update();
        return new PaymentResponse(payment);
    }

    @Override
    public PaymentResponse getPayments(Long userId) {
        var payment = repository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        return new PaymentResponse(payment);
    }


    @Override
    public PaymentResponse cancelTransaction(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        var payment = repository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        user.setSubscribed(false);
        payment.setExpired();
        userRepository.save(user);
        repository.save(payment);
        return new PaymentResponse(payment);
    }
}
