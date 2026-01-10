package br.com.souza.eda_order_service.service;

import br.com.souza.eda_order_service.database.model.OrderEntity;
import br.com.souza.eda_order_service.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(OrderEntity order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(order.getEmail());
            message.setSubject("Atualização do Pedido: " + order.getId());

            String statusMessage = order.getStatus().equals(OrderStatus.PAID)
                ? "Seu pedido foi aprovado com sucesso!"
                : "Infelizmente seu pedido foi cancelado. Por favor, verifique os dados de pagamento.";

            message.setText("Olá,\n\n" + statusMessage + "\n\nID do Pedido: " + order.getId() + "\nData: " + order.getCreatedAt());

            mailSender.send(message);
            log.info("E-mail enviado com sucesso para: {}", order.getEmail());
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail para {}: {}", order.getEmail(), e.getMessage());
        }
    }
}