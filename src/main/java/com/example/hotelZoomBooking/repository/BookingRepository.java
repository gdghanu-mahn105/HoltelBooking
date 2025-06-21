package com.example.hotelZoomBooking.repository;

import com.example.hotelZoomBooking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
    @Query(value = "SELECT b.room_id FROM bookings b WHERE b.begin_date <= :endDate AND b.end_date >= :startDate", nativeQuery = true)
    List<String> findBookedRoomIdsByDateRange(LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT r.room_id FROM hotel_room r " +
            "LEFT JOIN bookings b ON r.room_id = b.room_id " +
            "WHERE b.booking_id IS NULL " +
            "OR (b.begin_date > :endDate OR b.end_date < :startDate)",
            nativeQuery = true)
    List<Integer> findAvailableRoomIdsByDateRange(LocalDate startDate, LocalDate endDate);
}
