package br.com.souza.eda_order_service.service;

import br.com.souza.eda_order_service.database.model.OrderEntity;
import br.com.souza.eda_order_service.database.model.OrderItemsEntity;
import br.com.souza.eda_order_service.database.model.ProductEntity;
import br.com.souza.eda_order_service.database.repository.IOrderRepository;
import br.com.souza.eda_order_service.database.repository.IProductRepository;
import br.com.souza.eda_order_service.dto.*;
import br.com.souza.eda_order_service.enums.OrderStatus;
import br.com.souza.eda_order_service.enums.PaymentStatus;
import br.com.souza.eda_order_service.exception.InsufficientStockException;
import br.com.souza.eda_order_service.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersService {

    private final EmailService emailService;
    private final SnsService snsService;
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    @Value("${aws.sns.pedido-criado-topic-arn}")
    private String pedidoCriadoTopicArn;

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

            String message = OrderCreatedMessageDTO.builder()
                    .orderId(createdOrder.getId())
                    .totalPrice(createdOrder.getItems().stream()
                            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add))
                    .paymentType(request.getPaymentType())
                    .createdAt(createdOrder.getCreatedAt())
                    .build().toString();

            //enviar mensagem SNS
            log.info("Enviando mensagem SNS: {}", message);
            snsService.sendMessage(pedidoCriadoTopicArn, message);

            return new OrderResponseDTO(createdOrder);
        } catch (Exception e) {
            log.error("Erro ao criar pedido: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void updateOrderStatus(PaymentProcessedMessageDTO message) throws Exception {
        OrderEntity order = orderRepository.findById(message.getOrderId())
                .orElseThrow(() -> new NotFoundException(String.format("Pedido com id %s nao encontrado", message.getOrderId())));

        if (message.getPaymentStatus().equalsIgnoreCase(PaymentStatus.FRAUD_DETECTED.name()) ||
                message.getPaymentStatus().equalsIgnoreCase(PaymentStatus.INSUFFICIENT_BALANCE.name()) ||
                message.getPaymentStatus().equalsIgnoreCase(PaymentStatus.UNAVAILABLE_PAYMENT_TYPE.name())) {
            order.setStatus(OrderStatus.CANCELED);

            // Rollback do estoque: Devolvemos os itens para o produto
            for (OrderItemsEntity item : order.getItems()) {
                ProductEntity product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        } else {
            order.setStatus(OrderStatus.PAID);
        }

        //atualizar status pedido
        orderRepository.save(order);
        log.info("Status pedido atualizado: {}", order.getStatus());

        //enviar e-mail
        emailService.sendOrderConfirmation(order);
    }
}
