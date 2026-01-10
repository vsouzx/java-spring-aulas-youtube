package br.com.souza.eda_payment_service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCreatedMessageDTO {

    private UUID orderId;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;
    private String paymentType;

}
