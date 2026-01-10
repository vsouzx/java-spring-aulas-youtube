package br.com.souza.monolith_service.dto;

import br.com.souza.monolith_service.database.model.PaymentEntity;
import br.com.souza.monolith_service.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsResponseDTO {

    private UUID paymentId;
    private UUID orderId;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;

    public PaymentsResponseDTO(PaymentEntity entity) {
        this.paymentId = entity.getId();
        this.orderId = entity.getOrder().getId();
        this.paymentStatus = entity.getStatus();
        this.createdAt = entity.getCreatedAt();
    }
}
