package br.com.souza.rest_payment_service.database.repository;

import br.com.souza.rest_payment_service.database.model.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface IPaymentRepository extends MongoRepository<PaymentEntity, UUID> {
}
