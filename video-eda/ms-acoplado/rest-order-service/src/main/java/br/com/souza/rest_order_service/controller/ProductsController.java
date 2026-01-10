package br.com.souza.rest_order_service.controller;

import br.com.souza.rest_order_service.database.model.ProductEntity;
import br.com.souza.rest_order_service.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts(){
        return ResponseEntity.ok(productsService.getAllProducts());
    }
}
