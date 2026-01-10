package br.com.souza.monolith_service.service;

import br.com.souza.monolith_service.database.model.OrderEntity;
import br.com.souza.monolith_service.enums.OrderStatus;
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

    public void sendOrderConfirmation(OrderEntity order, String customerEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(customerEmail);
            message.setSubject("Atualização do Pedido: " + order.getId());

            String statusMessage = order.getStatus().equals(OrderStatus.PAID) 
                ? "Seu pedido foi aprovado com sucesso!"
                : "Infelizmente seu pedido foi cancelado. Por favor, verifique os dados de pagamento.";

            message.setText("Olá,\n\n" + statusMessage + "\n\nID do Pedido: " + order.getId() + "\nData: " + order.getCreatedAt());

            mailSender.send(message);
            log.info("E-mail enviado com sucesso para: {}", customerEmail);
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail para {}: {}", customerEmail, e.getMessage());
        }
    }
}