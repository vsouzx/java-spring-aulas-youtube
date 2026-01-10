package br.com.souza.rest_order_service.dto;

import br.com.souza.rest_order_service.database.model.OrderItemsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsResponseDTO {

    private UUID productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;

    public ItemsResponseDTO(OrderItemsEntity orderItemsEntity) {
        this.productId = orderItemsEntity.getProduct().getId();
        this.name = orderItemsEntity.getProduct().getName();
        this.quantity = orderItemsEntity.getQuantity();
        this.price = orderItemsEntity.getProduct().getPrice();
    }
}
