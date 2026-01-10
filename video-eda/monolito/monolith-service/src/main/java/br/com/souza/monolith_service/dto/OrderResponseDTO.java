package br.com.souza.monolith_service.dto;

import br.com.souza.monolith_service.database.model.OrderEntity;
import br.com.souza.monolith_service.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private UUID orderId;
    private List<ItemsResponseDTO> items;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;

    public OrderResponseDTO(OrderEntity order) {
        this.orderId = order.getId();
        this.items = order.getItems().stream().map(ItemsResponseDTO::new).toList();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.totalPrice = order.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
