package com.example.hotelZoomBooking.controller;

import com.example.hotelZoomBooking.dto.BookingRequest;
import com.example.hotelZoomBooking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/bookings") // đổi tên thành bookings
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    @GetMapping("/availability")
    public Map<String, Object> availabilityChecking(
            @RequestParam String roomType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return bookingService.availabilityChecking(roomType, startDate, endDate);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testing(@RequestParam(value = "user_id")String user_id){
        return ResponseEntity.ok(bookingService.testing(user_id));
    }

    @PostMapping("/addBookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        try {
            Map<String, Object> result = bookingService.createBooking(bookingRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
    @DeleteMapping("/{booking_id}")
    public ResponseEntity<?> cancelBooking(@PathVariable("booking_id") int booking_id,
                                           @RequestParam (value = "cancel_timestamp") String cancel_timestamp
    ) {
        return ResponseEntity.ok(bookingService.cancelBooking(booking_id, cancel_timestamp));
    }

}
