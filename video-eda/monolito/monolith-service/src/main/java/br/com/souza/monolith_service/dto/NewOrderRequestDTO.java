package br.com.souza.monolith_service.dto;

import br.com.souza.monolith_service.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderRequestDTO {

    private List<ItemsRequestDTO> items;
    private PaymentType paymentType;
    private String email;
}
