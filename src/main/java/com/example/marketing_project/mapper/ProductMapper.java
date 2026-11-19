package com.example.marketing_project.mapper;

import com.example.marketing_project.dto.ProductRequestDTO;
import com.example.marketing_project.dto.ProductResponseDTO;
import com.example.marketing_project.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity toEntity(ProductRequestDTO dto) {
        ProductEntity product = new ProductEntity();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setActive(dto.isActive());
        return product;
    }

    public void updateEntity(ProductEntity product, ProductRequestDTO dto) {
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setActive(dto.isActive());
    }

    public ProductResponseDTO toResponseDTO(ProductEntity entity) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setActive(entity.isActive());
        dto.setCreated_at(entity.getCreated_at());
        dto.setUpdated_at(entity.getUpdated_at());
        return dto;
    }
}
