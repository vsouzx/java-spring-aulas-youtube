package br.com.souza.eda_payment_service.consumer;

import br.com.souza.eda_payment_service.dto.OrderCreatedMessageDTO;
import br.com.souza.eda_payment_service.dto.SnsEnvelope;
import br.com.souza.eda_payment_service.service.PaymentsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedConsumer {

    private final PaymentsService paymentsService;
    private final ObjectMapper mapper;

    @SqsListener(value = "${aws.sqs.pedido-criado-queue}")
    public void consume(SnsEnvelope envelope) throws Exception {
        log.info("Mensagem recebida");
        OrderCreatedMessageDTO message = mapper.readValue(envelope.Message(), OrderCreatedMessageDTO.class);
        paymentsService.processOrderPayment(message);
    }
}
