package learning.platform.controller;

import jakarta.validation.Valid;
import learning.platform.config.TokenService;
import learning.platform.dto.PaymentRequest;
import learning.platform.dto.PaymentResponse;
import learning.platform.entity.User;
import learning.platform.service.impl.PaymentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentServiceImpl service;

    public PaymentController(PaymentServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> crearPago(@Valid @RequestBody PaymentRequest request){
        return ResponseEntity.ok(service.pay(request));
    }

    @PutMapping
    public  ResponseEntity<PaymentResponse> actualizarPago(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.updatePayment(user.getId()));
    }

    @GetMapping("/my-payments")
    public  ResponseEntity<PaymentResponse> obtenerMisPagos(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.getPayments(user.getId()));
    }

    @DeleteMapping
    public  ResponseEntity cancelarPago(@AuthenticationPrincipal User user){
        service.cancelTransaction(user.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}