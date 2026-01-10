package br.com.souza.monolith_service.database.repository;

import br.com.souza.monolith_service.database.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IPaymentRepository extends JpaRepository<PaymentEntity, UUID> {
}
