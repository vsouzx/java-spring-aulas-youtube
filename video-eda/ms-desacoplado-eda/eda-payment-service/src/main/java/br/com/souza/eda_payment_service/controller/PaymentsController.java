package br.com.souza.eda_payment_service.controller;

import br.com.souza.eda_payment_service.dto.PaymentsResponseDTO;
import br.com.souza.eda_payment_service.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;

    @GetMapping
    public ResponseEntity<List<PaymentsResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentsService.getAllPayments());
    }
}
