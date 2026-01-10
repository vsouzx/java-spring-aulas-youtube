package br.com.souza.eda_payment_service.service;

import br.com.souza.eda_payment_service.database.model.PaymentEntity;
import br.com.souza.eda_payment_service.database.repository.IPaymentRepository;
import br.com.souza.eda_payment_service.dto.OrderCreatedMessageDTO;
import br.com.souza.eda_payment_service.dto.PaymentProcessedMessageDTO;
import br.com.souza.eda_payment_service.dto.PaymentsResponseDTO;
import br.com.souza.eda_payment_service.enums.PaymentStatus;
import br.com.souza.eda_payment_service.enums.PaymentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentsService {

    private final SnsService snsService;
    private final IPaymentRepository paymentRepository;
    @Value("${aws.sns.pagamento-processado-topic}")
    private String processedPaymentTopic;
    private static final BigDecimal MAX_ORDER_AMOUNT = BigDecimal.valueOf(10_000);
    private static final BigDecimal CUSTOMER_BALANCE = BigDecimal.valueOf(5_000);

    public List<PaymentsResponseDTO> getAllPayments(){
        return paymentRepository.findAll().stream()
                .map(PaymentsResponseDTO::new)
                .toList();
    }

    public void processOrderPayment(OrderCreatedMessageDTO message) throws InterruptedException {
        try {
            log.info("Processando pagamento do pedido: {}", message.getOrderId());
            PaymentEntity payment = PaymentEntity.builder()
                    .paymentId(UUID.randomUUID())
                    .orderId(message.getOrderId())
                    .paymentType(PaymentType.fromName(message.getPaymentType()))
                    .createdAt(LocalDateTime.now())
                    .build();

            if (message.getPaymentType().equalsIgnoreCase(PaymentType.CREDIT_CARD.name()))
                payment.setStatus(PaymentStatus.UNAVAILABLE_PAYMENT_TYPE);
            else if (hasPossibleFraud(message.getCreatedAt(), message.getTotalPrice()))
                payment.setStatus(PaymentStatus.FRAUD_DETECTED);
            else if (hasInsufficientBalance(message.getTotalPrice()))
                payment.setStatus(PaymentStatus.INSUFFICIENT_BALANCE);
            else payment.setStatus(PaymentStatus.APPROVED);

            PaymentEntity savedPayment = paymentRepository.save(payment);
            log.info("Pagamento processado: {}", savedPayment.getStatus());

            PaymentProcessedMessageDTO paymentProcessedMessage = PaymentProcessedMessageDTO.builder()
                    .orderId(message.getOrderId())
                    .paymentId(savedPayment.getPaymentId())
                    .paymentStatus(savedPayment.getStatus())
                    .build();

            log.info("Enviando mensagem para SNS: {}", paymentProcessedMessage.toString());
            snsService.sendMessage(processedPaymentTopic, paymentProcessedMessage.toString());
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