package com.example.marketing_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebhookPurchaseCustomerRequestDTO {

    private String name;
    private String email;
    private String phone;
    private Integer age;
    private String gender;
    private String location;
}
