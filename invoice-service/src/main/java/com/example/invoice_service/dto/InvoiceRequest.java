package com.example.invoice_service.dto;
 
import lombok.*;
 
@Data
public class InvoiceRequest {
    private Long bookingId;
    private Long serviceTypeId;
}