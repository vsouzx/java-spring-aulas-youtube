package br.com.souza.rest_payment_service.dto;

import br.com.souza.rest_payment_service.database.model.PaymentEntity;
import br.com.souza.rest_payment_service.enums.PaymentStatus;
import br.com.souza.rest_payment_service.enums.PaymentType;
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
public class PaymentStatusResponse {

    private UUID paymentId;
    private PaymentStatus status;

    public PaymentStatusResponse(PaymentEntity entity) {
        this.paymentId = entity.getPaymentId();
        this.status = entity.getStatus();
    }
}
