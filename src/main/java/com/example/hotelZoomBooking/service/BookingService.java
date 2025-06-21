package com.example.hotelZoomBooking.service;

import com.example.hotelZoomBooking.dto.BookingRequest;
import com.example.hotelZoomBooking.entity.RoomTypeEntity;
import com.example.hotelZoomBooking.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookingService {
    Map<String, Object> availabilityChecking(String roomType, String startDate, String endDate);
    Map<String, Object> createBooking(BookingRequest request);
    Map<String, Object> cancelBooking(String booking_id);
    Optional<UserEntity> testing(String userId);
}
