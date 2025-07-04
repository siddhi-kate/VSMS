package com.example.invoice_service.dto;

import lombok.Data;

@Data
public class ServiceTypeResponse {
    private Long serviceTypeId;
    private String description;
    private Double price;
}
