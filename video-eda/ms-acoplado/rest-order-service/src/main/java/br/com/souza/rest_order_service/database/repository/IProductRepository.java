package br.com.souza.rest_order_service.database.repository;

import br.com.souza.rest_order_service.database.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProductRepository extends JpaRepository<ProductEntity, UUID> {
}
