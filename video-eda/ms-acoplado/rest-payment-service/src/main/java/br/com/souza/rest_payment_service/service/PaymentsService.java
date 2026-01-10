package br.com.souza.rest_payment_service.service;

import br.com.souza.rest_payment_service.database.model.PaymentEntity;
import br.com.souza.rest_payment_service.database.repository.IPaymentRepository;
import br.com.souza.rest_payment_service.dto.PaymentStatusResponse;
import br.com.souza.rest_payment_service.dto.PaymentsRequest;
import br.com.souza.rest_payment_service.dto.PaymentsResponseDTO;
import br.com.souza.rest_payment_service.enums.PaymentStatus;
import br.com.souza.rest_payment_service.enums.PaymentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    public PaymentStatusResponse processOrderPayment(PaymentsRequest request) throws InterruptedException {
        try {
//            Thread.sleep(10000);
            log.info("Processando pagamento: {}", request);
            PaymentEntity payment = PaymentEntity.builder()
                    .paymentId(UUID.randomUUID())
                    .orderId(request.getOrderId())
                    .paymentType(request.getPaymentType())
                    .createdAt(LocalDateTime.now())
                    .build();

            if (request.getPaymentType().equals(PaymentType.CREDIT_CARD))
                payment.setStatus(PaymentStatus.UNAVAILABLE_PAYMENT_TYPE);
            else if (hasPossibleFraud(request.getCreatedAt(), request.getTotalPrice()))
                payment.setStatus(PaymentStatus.FRAUD_DETECTED);
            else if (hasInsufficientBalance(request.getTotalPrice()))
                payment.setStatus(PaymentStatus.INSUFFICIENT_BALANCE);
            else payment.setStatus(PaymentStatus.APPROVED);

            PaymentEntity savedPayment = paymentRepository.save(payment);
            log.info("Pagamento processado: {}", savedPayment);
            return new PaymentStatusResponse(savedPayment);
        }catch (Exception e){
            log.error("Erro ao processar pagamento: {}", e.getMessage());
            throw e;
        }
    }

    private boolean hasPossibleFraud(LocalDateTime orderCreatedAt, BigDecimal totalOrderAmount) {
        // Regra 1: valor muito alto
        if (totalOrderAmount.compareTo(MAX_ORDER_AMOUNT) > 0) {
            log.info("Pedido suspeito: valor muito alto ({})", totalOrderAmount);
            return true;
        }

        // Regra 2: horário suspeito (ex: madrugada)
        int hour = orderCreatedAt.getHour();
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