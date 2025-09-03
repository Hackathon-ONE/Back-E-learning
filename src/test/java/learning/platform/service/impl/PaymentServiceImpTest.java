package learning.platform.service.impl;

import learning.platform.dto.PaymentRequest;
import learning.platform.dto.PaymentResponse;
import learning.platform.entity.Payment;
import learning.platform.entity.User;
import learning.platform.exception.ResourceNotFoundException;
import learning.platform.mapper.PaymentMapper;
import learning.platform.repository.PaymentRepository;
import learning.platform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private PaymentRepository paymentRepository;
    private UserRepository userRepository;
    private PaymentMapper paymentMapper; // si tu mapper hace transformaciones complejas lo puedes mockear

    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        userRepository = mock(UserRepository.class);
        paymentMapper = mock(PaymentMapper.class);

        paymentService = new PaymentServiceImpl(paymentRepository, userRepository, paymentMapper);
    }

    @Test
    void pay_ShouldSavePaymentAndUpdateUser() {
        // given
        var user = new User();
        user.setId(1L);
        user.setSubscribed(false);

        var request = new PaymentRequest(1L, BigDecimal.valueOf(200));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        PaymentResponse response = paymentService.pay(request);

        // then
        assertThat(user.isSubscribed()).isTrue();

        // Verificamos que guardó usuario y pago
        verify(userRepository).save(user);
        verify(paymentRepository).save(any(Payment.class));

        assertThat(response).isNotNull();
    }

    @Test
    void pay_ShouldThrowException_WhenUserNotFound() {
        // given
        var request = new PaymentRequest(99L, BigDecimal.valueOf(200));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(ResourceNotFoundException.class, () -> paymentService.pay(request));
    }

    @Test
    void updatePayment_ShouldUpdatePayment() {
        // given
        var user = new User();
        user.setId(1L);
        var payment = new Payment(new PaymentRequest(1L, BigDecimal.TEN), user);

        when(paymentRepository.findByUserId(1L)).thenReturn(Optional.of(payment));

        // when
        PaymentResponse response = paymentService.updatePayment(1L);

        // then
        verify(paymentRepository).findByUserId(1L);
        assertThat(response).isNotNull();
    }

    @Test
    void getPayments_ShouldReturnPayment() {
        var user = new User();
        user.setId(1L);
        var payment = new Payment(new PaymentRequest(1L, BigDecimal.TEN), user);

        when(paymentRepository.findByUserId(1L)).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.getPayments(1L);

        assertThat(response).isNotNull();
    }

    @Test
    void cancelTransaction_ShouldCancelPaymentAndUpdateUser() {
        // given
        var user = new User();
        user.setId(1L);
        user.setSubscribed(true);

        var payment = new Payment(new PaymentRequest(1L, BigDecimal.valueOf(100)), user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentRepository.findByUserId(1L)).thenReturn(Optional.of(payment));

        // when
        PaymentResponse response = paymentService.cancelTransaction(1L);

        // then
        assertThat(user.isSubscribed()).isFalse();

        verify(userRepository).save(user);
        verify(paymentRepository).save(payment);

        assertThat(response).isNotNull();
    }
}
