package br.com.souza.rest_payment_service.dto;

import br.com.souza.rest_payment_service.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentsRequest {

    @NotNull
    private UUID orderId;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private BigDecimal totalPrice;
    @NotNull
    private PaymentType paymentType;
}
