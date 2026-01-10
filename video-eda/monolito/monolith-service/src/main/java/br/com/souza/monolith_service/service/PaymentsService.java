package br.com.souza.monolith_service.service;

import br.com.souza.monolith_service.database.model.OrderEntity;
import br.com.souza.monolith_service.database.model.PaymentEntity;
import br.com.souza.monolith_service.database.repository.IPaymentRepository;
import br.com.souza.monolith_service.dto.PaymentsResponseDTO;
import br.com.souza.monolith_service.enums.PaymentStatus;
import br.com.souza.monolith_service.enums.PaymentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentsService {

    private static final BigDecimal MAX_ORDER_AMOUNT = BigDecimal.valueOf(10_000);
    private static final BigDecimal CUSTOMER_BALANCE = BigDecimal.valueOf(5_000);
    private final IPaymentRepository paymentRepository;

    public List<PaymentsResponseDTO> getAllPayments(){
        return paymentRepository.findAll().stream()
                .map(PaymentsResponseDTO::new)
                .toList();
    }

    public PaymentEntity processOrderPayment(OrderEntity order, PaymentType paymentType) throws InterruptedException {
        Thread.sleep(10000);
        PaymentEntity payment = PaymentEntity.builder()
                .order(order)
                .type(paymentType)
                .build();

        BigDecimal totalOrderAmount = order.getItems()
                .stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (paymentType.equals(PaymentType.CREDIT_CARD))
            payment.setStatus(PaymentStatus.UNAVAILABLE_PAYMENT_TYPE);
        else if (hasPossibleFraud(order, totalOrderAmount))
            payment.setStatus(PaymentStatus.FRAUD_DETECTED);
        else if (hasInsufficientBalance(totalOrderAmount))
            payment.setStatus(PaymentStatus.INSUFFICIENT_BALANCE);
        else payment.setStatus(PaymentStatus.APPROVED);

        return paymentRepository.save(payment);
    }

    private boolean hasPossibleFraud(OrderEntity order, BigDecimal totalOrderAmount) {
        // Regra 1: valor muito alto
        if (totalOrderAmount.compareTo(MAX_ORDER_AMOUNT) > 0) {
            log.info("Pedido suspeito: valor muito alto ({})", totalOrderAmount);
            return true;
        }

        // Regra 3: horário suspeito (ex: madrugada)
        int hour = order.getCreatedAt().getHour();
        if (hour < 6 || hour > 22) {
            log.info("Pedido suspeito: criado fora do horário padrão");
            return false;
        }

        return false;
    }

    private boolean hasInsufficientBalance(BigDecimal totalOrderAmount) {
        if (totalOrderAmount.compareTo(CUSTOMER_BALANCE) > 0) {
            log.info("Saldo insuficiente. Pedido: {} | Saldo: {}", totalOrderAmount, CUSTOMER_BALANCE);
            return true;
        }
        return false;
    }
}