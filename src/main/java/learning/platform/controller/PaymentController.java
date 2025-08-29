package learning.platform.controller;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import learning.platform.config.TokenService;
import learning.platform.dto.PaymentRequest;
import learning.platform.dto.PaymentResponse;
import learning.platform.service.impl.PaymentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Entity
@Table(name = "payments")
public class PaymentController {

    private final TokenService tokenService;
    private final PaymentServiceImpl service;

    public PaymentController(TokenService tokenService, PaymentServiceImpl service) {
        this.tokenService = tokenService;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> creaPago(@Valid PaymentRequest request){

    }
}