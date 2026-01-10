package br.com.souza.eda_order_service.controller;

import br.com.souza.eda_order_service.dto.OrderRequestDTO;
import br.com.souza.eda_order_service.dto.OrderResponseDTO;
import br.com.souza.eda_order_service.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) throws Exception {
        return ResponseEntity.ok(ordersService.createOrder(orderRequestDTO));
    }
}
