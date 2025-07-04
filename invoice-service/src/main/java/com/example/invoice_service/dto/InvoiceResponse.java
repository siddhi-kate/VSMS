package com.example.invoice_service.dto;
 
import lombok.*;

@Data
@Builder
public class InvoiceResponse {
    private Long invoiceId;
    private BookingResponse booking;
    private ServiceTypeResponse serviceType;
    private Double totalAmount;
    private String paymentStatus;
}