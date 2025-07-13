package com.example.hotelZoomBooking.service;

import com.example.hotelZoomBooking.dto.BookingRequest;
import com.example.hotelZoomBooking.entity.UserEntity;
import java.util.Map;
import java.util.Optional;

public interface BookingService {
    Map<String, Object> availabilityChecking(String roomType, String startDate, String endDate);
    Map<String, Object> createBooking(BookingRequest request);
    Map<String, Object> cancelBooking(int booking_id,String cancel_timestamp);
    Optional<UserEntity> testing(String userId);
}
