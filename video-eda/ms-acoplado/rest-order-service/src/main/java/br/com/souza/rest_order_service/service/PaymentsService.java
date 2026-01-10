package br.com.souza.rest_order_service.service;

import br.com.souza.rest_order_service.client.IPaymentsClient;
import br.com.souza.rest_order_service.dto.client.PaymentResponse;
import br.com.souza.rest_order_service.dto.client.PaymentsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentsService {

    private final IPaymentsClient paymentsClient;

    public PaymentResponse processPayment(PaymentsRequest paymentsRequest) {
        try{
            return paymentsClient.processPayment(paymentsRequest);
        }catch (Exception e){
            log.error("Erro ao processar pagamento: {}", e.getMessage(), e);
            throw e;
        }
    }
}
