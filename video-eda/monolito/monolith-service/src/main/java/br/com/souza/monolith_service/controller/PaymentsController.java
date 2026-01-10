package br.com.souza.monolith_service.controller;

import br.com.souza.monolith_service.database.model.PaymentEntity;
import br.com.souza.monolith_service.database.model.ProductEntity;
import br.com.souza.monolith_service.dto.PaymentsResponseDTO;
import br.com.souza.monolith_service.service.PaymentsService;
import br.com.souza.monolith_service.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;

    @GetMapping
    public ResponseEntity<List<PaymentsResponseDTO>> getAllPayments(){
        return ResponseEntity.ok(paymentsService.getAllPayments());
    }
}
