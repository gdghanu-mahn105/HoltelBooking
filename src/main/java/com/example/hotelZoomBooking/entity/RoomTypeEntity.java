package com.example.hotelZoomBooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_type")
public class RoomTypeEntity {
    @Id
    @Column(name = "room_type")
    private String roomType;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "normal_price")
    private double normalPrice;

    @Column(name = "weekends_price")
    private double weekendsPrice;

    @Column(name = "holiday_price")
    private double holidayPrice;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
