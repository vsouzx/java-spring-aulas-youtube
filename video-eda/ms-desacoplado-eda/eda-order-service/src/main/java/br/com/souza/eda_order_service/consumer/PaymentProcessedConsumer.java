package br.com.souza.eda_order_service.consumer;

import br.com.souza.eda_order_service.dto.PaymentProcessedMessageDTO;
import br.com.souza.eda_order_service.dto.SnsEnvelope;
import br.com.souza.eda_order_service.service.OrdersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessedConsumer {

    private final OrdersService ordersService;
    private final ObjectMapper mapper;

    @SqsListener(value = "${aws.sqs.pagamento-processado-queue}")
    public void consume(SnsEnvelope envelope) throws Exception {
        log.info("Mensagem recebida");
        PaymentProcessedMessageDTO message = mapper.readValue(envelope.Message(), PaymentProcessedMessageDTO.class);
        ordersService.updateOrderStatus(message);
    }
}
