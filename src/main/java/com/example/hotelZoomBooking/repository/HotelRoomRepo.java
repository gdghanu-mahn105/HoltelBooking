package com.example.hotelZoomBooking.repository;

import com.example.hotelZoomBooking.entity.HotelRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRoomRepo  extends JpaRepository<HotelRoomEntity, Integer> {

    List<HotelRoomEntity> findByRoomTypeEntityRoomType(String room_type);


    HotelRoomEntity findById(int roomId);
    @Query("SELECT h.roomId FROM HotelRoomEntity h " +
            "LEFT JOIN BookingEntity b ON h.roomId = b.hotelRoomEntity.roomId " +
            "WHERE h.roomTypeEntity.roomType = :roomType " +
            "AND (b IS NULL OR (b.begin_date > :endDate OR b.end_date < :startDate))")
    List<Integer> findAvailableRoomIdsByRoomTypeAndDateRange(
            @Param("roomType") String roomType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


}
