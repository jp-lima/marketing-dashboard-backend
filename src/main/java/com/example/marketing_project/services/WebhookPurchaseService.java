package com.example.marketing_project.services;

import com.example.marketing_project.dto.WebhookPurchaseCustomerRequestDTO;
import com.example.marketing_project.dto.WebhookPurchaseItemRequestDTO;
import com.example.marketing_project.dto.WebhookPurchaseRequestDTO;
import com.example.marketing_project.entity.CampaignEntity;
import com.example.marketing_project.entity.CustomerEntity;
import com.example.marketing_project.entity.ProductEntity;
import com.example.marketing_project.entity.PurchaseEntity;
import com.example.marketing_project.entity.PurchaseItemEntity;
import com.example.marketing_project.enums.Platform;
import com.example.marketing_project.enums.PurchaseStatus;
import com.example.marketing_project.repository.CampaignRepository;
import com.example.marketing_project.repository.CustomerRepository;
import com.example.marketing_project.repository.ProductRepository;
import com.example.marketing_project.repository.PurchaseItemRepository;
import com.example.marketing_project.repository.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WebhookPurchaseService {

    private final CampaignRepository campaignRepository;
    private final CustomerRepository customerRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ProductRepository productRepository;

    public WebhookPurchaseService(CampaignRepository campaignRepository,
                                  CustomerRepository customerRepository,
                                  PurchaseRepository purchaseRepository,
                                  PurchaseItemRepository purchaseItemRepository,
                                  ProductRepository productRepository) {
        this.campaignRepository = campaignRepository;
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void processPurchase(WebhookPurchaseRequestDTO dto) {
        validateRequest(dto);

        CampaignEntity campaign = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campanha ou produto não encontrado"));

        List<WebhookPurchaseItemRequestDTO> items = dto.getItems();
        validateProducts(items);

        BigDecimal calculatedTotal = calculateTotal(items);
        if (calculatedTotal.compareTo(dto.getTotalAmount()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        CustomerEntity customer = createCustomer(dto.getCustomer(), campaign);
        PurchaseEntity purchase = createPurchase(dto, customer, campaign);
        createPurchaseItems(items, purchase);
    }

    private void validateRequest(WebhookPurchaseRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        if (dto.getCampaignId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        if (dto.getPlatform() == null || dto.getPlatform().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        try {
            Platform.valueOf(dto.getPlatform().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        if (dto.getCustomer() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        WebhookPurchaseCustomerRequestDTO customer = dto.getCustomer();
        if (customer.getName() == null || customer.getName().isBlank()
                || customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        for (WebhookPurchaseItemRequestDTO item : dto.getItems()) {
            if (item.getProductId() == null
                    || item.getUnitPrice() == null
                    || item.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0
                    || item.getQuantity() == null
                    || item.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
            }
        }

        if (dto.getTotalAmount() == null || dto.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }

        if (dto.getPurchasedAt() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos");
        }
    }

    private void validateProducts(List<WebhookPurchaseItemRequestDTO> items) {
        for (WebhookPurchaseItemRequestDTO item : items) {
            productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campanha ou produto não encontrado"));
        }
    }

    private BigDecimal calculateTotal(List<WebhookPurchaseItemRequestDTO> items) {
        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CustomerEntity createCustomer(WebhookPurchaseCustomerRequestDTO dto, CampaignEntity campaign) {
        CustomerEntity customer = new CustomerEntity();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAge(dto.getAge());
        customer.setGender(dto.getGender());
        customer.setLocation(dto.getLocation());
        customer.setSourceCampaign(campaign);
        return customerRepository.save(customer);
    }

    private PurchaseEntity createPurchase(WebhookPurchaseRequestDTO dto,
                                          CustomerEntity customer,
                                          CampaignEntity campaign) {
        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setCustomer(customer);
        purchase.setCampaign(campaign);
        purchase.setTotalAmount(dto.getTotalAmount());
        purchase.setStatus(PurchaseStatus.PAID);
        purchase.setPurchasedAt(LocalDateTime.ofInstant(dto.getPurchasedAt(), ZoneOffset.UTC));
        return purchaseRepository.save(purchase);
    }

    private void createPurchaseItems(List<WebhookPurchaseItemRequestDTO> items, PurchaseEntity purchase) {
        List<PurchaseItemEntity> purchaseItems = new ArrayList<>();

        for (WebhookPurchaseItemRequestDTO item : items) {
            PurchaseItemEntity purchaseItem = new PurchaseItemEntity();
            purchaseItem.setPurchase(purchase);
            purchaseItem.setProductId(item.getProductId());
            purchaseItem.setUnitPrice(item.getUnitPrice());
            purchaseItem.setQuantity(item.getQuantity());
            purchaseItem.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            purchaseItems.add(purchaseItem);
        }

        purchaseItemRepository.saveAll(purchaseItems);
    }
}
