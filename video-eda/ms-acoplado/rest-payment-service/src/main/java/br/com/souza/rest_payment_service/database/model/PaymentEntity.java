package br.com.souza.rest_payment_service.database.model;

import br.com.souza.rest_payment_service.enums.PaymentStatus;
import br.com.souza.rest_payment_service.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "payments")
public class PaymentEntity {

    @Id
    private UUID paymentId;
    private UUID orderId;
    private PaymentStatus status;
    private PaymentType paymentType;
    private LocalDateTime createdAt;

}
