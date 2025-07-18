package com.example.hotelZoomBooking.repository;

import com.example.hotelZoomBooking.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, String> {
}
