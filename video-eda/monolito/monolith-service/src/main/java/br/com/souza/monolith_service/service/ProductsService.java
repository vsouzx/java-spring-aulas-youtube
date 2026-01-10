package br.com.souza.monolith_service.service;

import br.com.souza.monolith_service.database.model.ProductEntity;
import br.com.souza.monolith_service.database.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final IProductRepository productRepository;

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
}
