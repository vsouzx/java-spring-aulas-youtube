package br.com.souza.monolith_service.service;

import br.com.souza.monolith_service.database.model.OrderEntity;
import br.com.souza.monolith_service.database.model.OrderItemsEntity;
import br.com.souza.monolith_service.database.model.PaymentEntity;
import br.com.souza.monolith_service.database.model.ProductEntity;
import br.com.souza.monolith_service.database.repository.IOrderRepository;
import br.com.souza.monolith_service.database.repository.IProductRepository;
import br.com.souza.monolith_service.dto.ItemsRequestDTO;
import br.com.souza.monolith_service.dto.NewOrderRequestDTO;
import br.com.souza.monolith_service.dto.OrderResponseDTO;
import br.com.souza.monolith_service.enums.OrderStatus;
import br.com.souza.monolith_service.enums.PaymentStatus;
import br.com.souza.monolith_service.exception.InsufficientStockException;
import br.com.souza.monolith_service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public OrderResponseDTO createOrder(NewOrderRequestDTO request) throws Exception {
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

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
        PaymentEntity validatedPayment = paymentsService.processOrderPayment(createdOrder, request.getPaymentType());
        log.info("Pagamento validado: {}", validatedPayment.getStatus());

        if (validatedPayment.getStatus().equals(PaymentStatus.FRAUD_DETECTED) ||
                validatedPayment.getStatus().equals(PaymentStatus.INSUFFICIENT_BALANCE)) {
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
        emailService.sendOrderConfirmation(createdOrder, request.getEmail());

        return new OrderResponseDTO(createdOrder);
    }
}
