package com.example.invoice_service.dto;

import java.time.LocalDate;

import lombok.*;

@Data
public class BookingResponse {
    private Long id;
    UserResponse userResponse;
    VehicleResponse vehicleResponse;
    // private Long serviceCenterId;
    private LocalDate date;
    private String timeSlot;
    private String status;
}
