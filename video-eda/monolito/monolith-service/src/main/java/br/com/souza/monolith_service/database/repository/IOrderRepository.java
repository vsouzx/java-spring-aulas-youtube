package br.com.souza.monolith_service.database.repository;

import br.com.souza.monolith_service.database.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
