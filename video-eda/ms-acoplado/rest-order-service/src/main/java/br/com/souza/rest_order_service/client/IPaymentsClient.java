package br.com.souza.rest_order_service.client;

import br.com.souza.rest_order_service.dto.client.PaymentResponse;
import br.com.souza.rest_order_service.dto.client.PaymentsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payments-service", url = "${payments.service.url}")
public interface IPaymentsClient {

    @PostMapping("/v1/payments")
    PaymentResponse processPayment(@RequestBody PaymentsRequest paymentsRequest);
}
