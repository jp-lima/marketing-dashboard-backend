package com.example.marketing_project.services;

import com.example.marketing_project.dto.ProductRequestDTO;
import com.example.marketing_project.dto.ProductResponseDTO;
import com.example.marketing_project.entity.ProductEntity;
import com.example.marketing_project.mapper.ProductMapper;
import com.example.marketing_project.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponseDTO create(ProductRequestDTO dto) {
        ProductEntity product = productMapper.toEntity(dto);
        return productMapper.toResponseDTO(productRepository.save(product));
    }

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    public ProductResponseDTO findById(UUID id) {
        return productMapper.toResponseDTO(findEntityById(id));
    }

    public ProductResponseDTO update(UUID id, ProductRequestDTO dto) {
        ProductEntity product = findEntityById(id);
        productMapper.updateEntity(product, dto);
        return productMapper.toResponseDTO(productRepository.save(product));
    }

    public void delete(UUID id) {
        productRepository.delete(findEntityById(id));
    }

    private ProductEntity findEntityById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado"
                ));
    }
}
