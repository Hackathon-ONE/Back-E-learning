package learning.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import learning.platform.dto.PaymentResponse;
import learning.platform.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Pagos", description = "Gestión de pagos por administradores")
public class AdminPaymentController {
    private final PaymentService paymentService;
    public AdminPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    /**
     * Listar todos los pagos registrados en la plataforma
     *
     * @return Lista de pagos con información resumida
     */
    @Operation(summary = "Obtener todos los pagos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagos listados correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado para usuarios no administradores")
    })
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.findAll();
        return ResponseEntity.ok(payments);
    }
}