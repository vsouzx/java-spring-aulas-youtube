package br.com.souza.rest_order_service.database.repository;

import br.com.souza.rest_order_service.database.model.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOrderItemsRepository extends JpaRepository<OrderItemsEntity, UUID> {
}
