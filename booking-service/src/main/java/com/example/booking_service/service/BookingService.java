package com.example.booking_service.service;

import com.example.booking_service.client.UserClient;
import com.example.booking_service.client.VehicleClient;
import com.example.booking_service.client.ServiceCenterClient;
import com.example.booking_service.dto.*;
import com.example.booking_service.entity.Booking;
import com.example.booking_service.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserClient userClient;
    private final VehicleClient vehicleClient;
    private final ServiceCenterClient serviceCenterClient;

    public BookingResponse createBooking(BookingRequest request) {
        // Validate user and vehicle
        UserResponse user = userClient.getUserById(request.getUserId());
        VehicleResponse vehicle = vehicleClient.getOne(request.getVehicleId());
        ServiceCenterResponse serviceCenter=serviceCenterClient.getServiceCenterById(request.getServiceCenterId());

        // Create booking entity
        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .vehicleId(request.getVehicleId())
                .serviceCenterId(request.getServiceCenterId())
                .bookingDate(request.getBookingDate())
                .status(request.getStatus())
                .build();

        // Save booking
        booking = bookingRepository.save(booking);
        log.info("Booking created with ID: {}", booking.getId());

        // Convert to response
        return toResponse(booking, user, vehicle,serviceCenter);
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(booking -> {
                    UserResponse user = userClient.getUserById(booking.getUserId());
                    VehicleResponse vehicle = vehicleClient.getOne(booking.getVehicleId());
                    ServiceCenterResponse serviceCenter=serviceCenterClient.getServiceCenterById(booking.getServiceCenterId());
                    return toResponse(booking, user, vehicle,serviceCenter);
                })
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    UserResponse user = userClient.getUserById(booking.getUserId());
                    VehicleResponse vehicle = vehicleClient.getOne(booking.getVehicleId());
                    ServiceCenterResponse serviceCenter=serviceCenterClient.getServiceCenterById(booking.getServiceCenterId());
                    return toResponse(booking, user, vehicle,serviceCenter);
                })
                .orElse(null);
    }

    public List<BookingResponse> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(booking -> {
                    UserResponse user = userClient.getUserById(booking.getUserId());
                    VehicleResponse vehicle = vehicleClient.getOne(booking.getVehicleId());
                    ServiceCenterResponse serviceCenter=serviceCenterClient.getServiceCenterById(booking.getServiceCenterId());
                    return toResponse(booking, user, vehicle,serviceCenter);
                })
                .collect(Collectors.toList());
    }

    public BookingResponse updateBooking(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setBookingDate(request.getBookingDate());
        booking.setStatus(request.getStatus());
        booking = bookingRepository.save(booking);

        UserResponse user = userClient.getUserById(booking.getUserId());
        VehicleResponse vehicle = vehicleClient.getOne(booking.getVehicleId());
        ServiceCenterResponse serviceCenter=serviceCenterClient.getServiceCenterById(request.getServiceCenterId());
        return toResponse(booking, user, vehicle,serviceCenter);
    }

    public void deleteBooking(Long id) {
        log.info("Deleting booking with ID: {}", id);
        bookingRepository.deleteById(id);
    }

    private BookingResponse toResponse(Booking booking, UserResponse user, VehicleResponse vehicle, ServiceCenterResponse serviceCenter) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUserId()) // Include userId
                .vehicleId(booking.getVehicleId()) // Include vehicleId
                .serviceCenterId(booking.getServiceCenterId())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .user(user)
                .vehicle(vehicle)
                .serviceCenter(serviceCenter)
                .build();
    }
}