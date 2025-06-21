package com.example.hotelZoomBooking.repository;

import com.example.hotelZoomBooking.entity.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepo extends JpaRepository<HolidayEntity, Integer> {
}
