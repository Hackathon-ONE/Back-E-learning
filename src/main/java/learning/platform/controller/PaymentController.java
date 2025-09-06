package learning.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payments", description = "Operaciones para gestionar las subscripcioes de estudiantes a la plataforma")
public class PaymentController {

    private final PaymentServiceImpl service;

    public PaymentController(PaymentServiceImpl service) {
        this.service = service;
    }

    @Operation(summary = "Crear un nuevo pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> crearPago(@Valid @RequestBody PaymentRequest request){
        return ResponseEntity.ok(service.pay(request));
    }

    @Operation(summary = "Actualizar el pago del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud Invalida")
    })
    @PutMapping
    public  ResponseEntity<PaymentResponse> actualizarPago(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.updatePayment(user.getId()));
    }

    @Operation(summary = "Obtener los pagos del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagos obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud Invalida")
    })
    @GetMapping("/my-payments")
    public  ResponseEntity<PaymentResponse> obtenerMisPagos(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.getPayments(user.getId()));
    }

    @Operation(summary = "Cancelar el pago del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Pago cancelado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud Invalida")
    })
    @DeleteMapping
    public  ResponseEntity cancelarPago(@AuthenticationPrincipal User user){
        service.cancelTransaction(user.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}