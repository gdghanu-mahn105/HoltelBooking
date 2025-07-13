package com.example.hotelZoomBooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int booking_id;


//    @Column(name = "booking_id")
//    private String booking_id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private HotelRoomEntity hotelRoomEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "begin_date")
    private LocalDate begin_date;

    @Column(name = "end_date")
    private LocalDate end_date;

    @ManyToOne
    @JoinColumn(name = "discount_code")
    private DiscountEntity discountEntity;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "booking_status")
    private String bookingStatus;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;




}
