package br.com.souza.rest_order_service.dto;

import br.com.souza.rest_order_service.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    private List<ItemsRequestDTO> items;
    private PaymentType paymentType;
    private String email;
}
