package com.example.hotelZoomBooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Hotel_room")
public class HotelRoomEntity {
    @Id
    @Column(name = "room_id")
    private int roomId;

    @ManyToOne
    @JoinColumn(name = "room_type")
    private RoomTypeEntity roomTypeEntity;

    @Column(name = "room_Status")
    private String roomStatus;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
