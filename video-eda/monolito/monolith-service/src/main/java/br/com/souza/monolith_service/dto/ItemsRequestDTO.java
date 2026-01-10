package br.com.souza.monolith_service.dto;

import br.com.souza.monolith_service.database.model.OrderItemsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsRequestDTO {

    private UUID productId;
    private Integer quantity;

    public ItemsRequestDTO(OrderItemsEntity orderItemsEntity) {
        this.productId = orderItemsEntity.getProduct().getId();
        this.quantity = orderItemsEntity.getQuantity();
    }
}
