package br.com.souza.rest_order_service.service;

import br.com.souza.rest_order_service.database.model.OrderEntity;
import br.com.souza.rest_order_service.database.model.OrderItemsEntity;
import br.com.souza.rest_order_service.database.model.ProductEntity;
import br.com.souza.rest_order_service.database.repository.IOrderRepository;
import br.com.souza.rest_order_service.database.repository.IProductRepository;
import br.com.souza.rest_order_service.dto.ItemsRequestDTO;
import br.com.souza.rest_order_service.dto.OrderRequestDTO;
import br.com.souza.rest_order_service.dto.OrderResponseDTO;
import br.com.souza.rest_order_service.dto.client.PaymentResponse;
import br.com.souza.rest_order_service.dto.client.PaymentsRequest;
import br.com.souza.rest_order_service.enums.OrderStatus;
import br.com.souza.rest_order_service.enums.PaymentStatus;
import br.com.souza.rest_order_service.exception.InsufficientStockException;
import br.com.souza.rest_order_service.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersService {

    private final PaymentsService paymentsService;
    private final EmailService emailService;
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderResponseDTO::new).toList();
    }

    @Transactional(rollbackOn = Exception.class)
    public OrderResponseDTO createOrder(OrderRequestDTO request) throws Exception {
        try {
            OrderEntity order = new OrderEntity();
            order.setStatus(OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            order.setEmail(request.getEmail());

            for (ItemsRequestDTO item : request.getItems()) {
                log.info("Validando Item: {}", item);
                ProductEntity product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new NotFoundException("Product not found"));

                if (product.getQuantity() < item.getQuantity())
                    throw new InsufficientStockException(String.format("Insufficient quantity in stock for product %s", product.getName()));

                OrderItemsEntity orderItem = new OrderItemsEntity();
                orderItem.setProduct(product);
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(product.getPrice());

                order.addItem(orderItem);

                //atualizar estoque
                product.setQuantity(product.getQuantity() - item.getQuantity());
                productRepository.save(product);
            }

            //salvar pedido
            OrderEntity createdOrder = orderRepository.save(order);
            log.info("Pedido criado");

            //validar pagamento
            PaymentResponse validatedPayment = paymentsService.processPayment(PaymentsRequest.builder()
                    .orderId(createdOrder.getId())
                    .totalPrice(order.getItems().stream()
                            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add))
                    .paymentType(request.getPaymentType())
                    .createdAt(createdOrder.getCreatedAt())
                    .build());
            log.info("Pagamento validado: {}", validatedPayment.getStatus());

            if (validatedPayment.getStatus().equalsIgnoreCase(PaymentStatus.FRAUD_DETECTED.name()) ||
                    validatedPayment.getStatus().equalsIgnoreCase(PaymentStatus.INSUFFICIENT_BALANCE.name())) {
                createdOrder.setStatus(OrderStatus.CANCELED);

                // Rollback do estoque: Devolvemos os itens para o produto
                for (OrderItemsEntity item : createdOrder.getItems()) {
                    ProductEntity product = item.getProduct();
                    product.setQuantity(product.getQuantity() + item.getQuantity());
                    productRepository.save(product);
                }
            } else {
                createdOrder.setStatus(OrderStatus.PAID);
            }

            //atualizar status pedido
            orderRepository.save(createdOrder);
            log.info("Status pedido atualizado: {}", createdOrder.getStatus());

            //enviar e-mail
            emailService.sendOrderConfirmation(createdOrder);

            return new OrderResponseDTO(createdOrder);
        } catch (Exception e) {
            log.error("Erro ao criar pedido: {}", e.getMessage());
            throw e;
        }
    }
}
