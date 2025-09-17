package learning.platform.service.impl;

import jakarta.transaction.Transactional;
import learning.platform.dto.PaymentRequest;
import learning.platform.dto.PaymentResponse;
import learning.platform.entity.Payment;
import learning.platform.exception.ResourceNotFoundException;
import learning.platform.helper.AuditContext;
import learning.platform.helper.AuditPropagator;
import learning.platform.mapper.PaymentMapper;
import learning.platform.repository.PaymentRepository;
import learning.platform.repository.UserRepository;
import learning.platform.service.PaymentService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final UserRepository userRepository;
    private final AuditContext auditContext;
    private final PaymentMapper mapper;
    private final AuditPropagator auditPropagator;

    public PaymentServiceImpl(PaymentRepository repository, UserRepository userRepository, AuditContext auditContext, PaymentMapper mapper, AuditPropagator auditPropagator) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.auditContext = auditContext;
        this.mapper = mapper;
        this.auditPropagator = auditPropagator;
    }


    @Override
    @Transactional
    public PaymentResponse pay(PaymentRequest paymentRequest) {
        auditContext.setCurrentUser("user:" + paymentRequest.userId());
        auditContext.setSessionId("session-" + System.currentTimeMillis());

        auditPropagator.propagate();

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
        auditContext.setCurrentUser("user:" + userId);
        auditContext.setSessionId("session-" + System.currentTimeMillis());

        auditPropagator.propagate();
        var payment = repository.findTopByUserIdOrderByPaidAtDesc(userId).orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        payment.update();
        return new PaymentResponse(payment);
    }

    @Override
    public PaymentResponse getPayments(Long userId) {
        var payment = repository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        return new PaymentResponse(payment);
    }


    @Override
    @Transactional
    public PaymentResponse cancelTransaction(Long userId) {
        auditContext.setCurrentUser("user:" + userId);
        auditContext.setSessionId("session-" + System.currentTimeMillis());

        auditPropagator.propagate();

        var user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        var payment = repository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        user.setSubscribed(false);
        payment.setExpired();
        userRepository.save(user);
        repository.save(payment);
        return new PaymentResponse(payment);
    }

    @Override
    public List<PaymentResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(PaymentResponse::new)
                .toList();
    }
}
