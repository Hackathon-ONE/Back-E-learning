package learning.platform.service;

import learning.platform.dto.PaymentRequest;
import learning.platform.dto.PaymentResponse;
import learning.platform.entity.Payment;
import learning.platform.entity.User;
import learning.platform.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentService() {
            @Override
            public PaymentResponse pay(PaymentRequest paymentRequest) {
                return null;
            }

            @Override
            public PaymentResponse updatePayment(Long userId) {
                return null;
            }

            @Override
            public PaymentResponse getPayments(Long userId) {
                return null;
            }

            @Override
            public PaymentResponse cancelTransaction(Long UserId) {
                return null;
            }

            @Override
            public List<PaymentResponse> findAll() {
                return List.of();
            }
        };
    }

    @Test
    void shouldReturnAllPayments() {
        User user = new User(1L, "Juan Pérez", "juan@test.com");
        Payment payment1 = new Payment(
                1L,
                user,
                new BigDecimal("100.50"),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30)
        );


        when(paymentRepository.findAll()).thenReturn(List.of(payment1));

        List<PaymentResponse> payments = paymentService.findAll();

        assertNotNull(payments);
        assertEquals(1, payments.size());
        assertEquals(1L, payments.get(0).userId());
        assertEquals(new BigDecimal("100.50"), payments.get(0).amount());
    }
}