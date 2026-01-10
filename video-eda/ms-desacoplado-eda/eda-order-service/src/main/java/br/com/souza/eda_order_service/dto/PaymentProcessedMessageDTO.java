package br.com.souza.eda_order_service.dto;

import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentProcessedMessageDTO {

    private UUID orderId;
    private UUID paymentId;
    private String paymentStatus;

}
