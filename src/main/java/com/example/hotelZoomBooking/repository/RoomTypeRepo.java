package com.example.hotelZoomBooking.repository;


import com.example.hotelZoomBooking.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepo extends JpaRepository<RoomTypeEntity, String> {
    RoomTypeEntity findByRoomType(String room_type);
}
