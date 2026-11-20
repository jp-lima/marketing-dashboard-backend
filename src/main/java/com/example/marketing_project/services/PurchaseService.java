package com.example.marketing_project.services;

import com.example.marketing_project.dto.PurchaseResponseDTO;
import com.example.marketing_project.entity.PurchaseEntity;
import com.example.marketing_project.mapper.PurchaseMapper;
import com.example.marketing_project.repository.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseService(PurchaseRepository purchaseRepository,
                           PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
    }

    @Transactional(readOnly = true)
    public List<PurchaseResponseDTO> findAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(purchaseMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PurchaseResponseDTO findPurchaseById(UUID id) {
        PurchaseEntity purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada"));
        return purchaseMapper.toResponseDTO(purchase);
    }
}
