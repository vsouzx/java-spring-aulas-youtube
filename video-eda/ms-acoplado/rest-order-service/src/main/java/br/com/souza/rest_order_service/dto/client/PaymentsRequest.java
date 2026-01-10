package br.com.souza.rest_order_service.dto.client;

import br.com.souza.rest_order_service.enums.PaymentType;
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

    private UUID orderId;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;
    private PaymentType paymentType;

}
