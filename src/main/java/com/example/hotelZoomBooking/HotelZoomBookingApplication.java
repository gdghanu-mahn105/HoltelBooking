package com.example.hotelZoomBooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.example.hotelZoomBooking.controller", "com.example.hotelZoomBooking.service"})
public class HotelZoomBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelZoomBookingApplication.class, args);
	}

}
