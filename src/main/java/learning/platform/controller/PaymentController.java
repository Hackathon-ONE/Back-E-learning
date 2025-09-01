package learning.platform.controller;

import jakarta.validation.Valid;
import learning.platform.config.TokenService;
import learning.platform.dto.PaymentRequest;
import learning.platform.dto.PaymentResponse;
import learning.platform.service.impl.PaymentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final TokenService tokenService;
    private final PaymentServiceImpl service;

    public PaymentController(TokenService tokenService, PaymentServiceImpl service) {
        this.tokenService = tokenService;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> crearPago(@Valid @RequestBody PaymentRequest request){
        return ResponseEntity.ok(service.pay(request));
    }

    @PutMapping
    public  ResponseEntity<PaymentResponse> actualizarPago(@RequestHeader("Authorization") String token){
        var userId = tokenService.getClaimId(token);
        return ResponseEntity.ok(service.updatePayment(userId));
    }

    @GetMapping("/my-payments")
    public  ResponseEntity<PaymentResponse> obtenerMisPagos(@RequestHeader("Authorization") String token){
        var userId = tokenService.getClaimId(token);
        return ResponseEntity.ok(service.getPayments(userId));
    }

    @DeleteMapping
    public  ResponseEntity cancelarPago(@RequestHeader("Authorization") String token){
        var userId = tokenService.getClaimId(token);
        service.cancelTransaction(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}