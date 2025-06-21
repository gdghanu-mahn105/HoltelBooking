package com.example.hotelZoomBooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRequest {

        private String user_id;
        private int room_id;
        private String room_type;
        private String start_date;
        private String end_date;
        @JsonProperty("is_VIP")
        private boolean is_VIP;
        private String discount_code;
        private OffsetDateTime requestTimestamp;
}
