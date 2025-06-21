package com.example.hotelZoomBooking.repository;

import com.example.hotelZoomBooking.dto.BookingRequest;
import com.example.hotelZoomBooking.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiscountRepo extends JpaRepository<DiscountEntity, String> {
//    List<String> findByDiscountCode(BookingRequest discountCode);

    List<DiscountEntity> findByDiscountCode(String discountCode);
}
