package br.com.souza.eda_payment_service.dto;

import br.com.souza.eda_payment_service.enums.PaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentProcessedMessageDTO {

    private UUID orderId;
    private UUID paymentId;
    private PaymentStatus paymentStatus;

    @Override
    @SneakyThrows
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }

}
