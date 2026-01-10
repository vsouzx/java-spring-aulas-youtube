package br.com.souza.eda_order_service.dto;

import br.com.souza.eda_order_service.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.ObjectMapper;

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
    private PaymentType paymentType;

    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}
