package com.example.hotelZoomBooking.service.bookingServiceImpl;

import com.example.hotelZoomBooking.dto.BookingRequest;
import com.example.hotelZoomBooking.entity.*;
import com.example.hotelZoomBooking.repository.*;
import com.example.hotelZoomBooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {


    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private DiscountRepo discountRepo;
    @Autowired
    private HolidayRepo holidayRepo;
    @Autowired
    private HotelRoomRepo hotelRoomRepo;
    @Autowired
    private RoomTypeRepo roomTypeRepo;
    @Autowired
    private UserRepo userRepo;
    @Override
    public Map<String, Object> availabilityChecking(String roomType, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        long days = ChronoUnit.DAYS.between(start, end);

        if (start.isBefore(LocalDate.now()) || start.isAfter(end) || (days < 1 || days > 7)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid date range");
        }

        RoomTypeEntity roomTypeEntity = roomTypeRepo.findByRoomType(roomType);
        if (roomTypeEntity == null) {
            throw new IllegalArgumentException("Room type not found: " + roomType);
        }

        List<Integer> availableRoomIds = hotelRoomRepo.findAvailableRoomIdsByRoomTypeAndDateRange(roomType, start, end);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("room_type", roomType);
        response.put("start_date", start);
        response.put("end_date", end);
        response.put("available_rooms", availableRoomIds);
        response.put("message", availableRoomIds.isEmpty() ? "No available rooms" : "Rooms available");

        return response;
    }

    @Override
    public Optional<UserEntity> testing(String userId) {
        return userRepo.findById(userId);
    }
    @Override
    public Map<String, Object> createBooking(BookingRequest request) {
        LocalDate startDate= LocalDate.parse(request.getStart_date()); // validate
        LocalDate endDate= LocalDate.parse(request.getEnd_date()); // validate
        OffsetDateTime requestTimestamp;
        if (request.getRequestTimestamp() != null) {
            requestTimestamp = request.getRequestTimestamp();
        } else {
            requestTimestamp = OffsetDateTime.now(ZoneOffset.UTC);
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (startDate.isBefore(LocalDate.now())|| startDate.isAfter(endDate)||(days<1 || days>7)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid date range");
        }

        UserEntity user= userRepo.getById(request.getUser_id());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found");
        }
        RoomTypeEntity roomType = roomTypeRepo.findById(request.getRoom_type())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid room type"));

        List<Integer> availableRoomIds = hotelRoomRepo.findAvailableRoomIdsByRoomTypeAndDateRange(request.getRoom_type(), startDate, endDate);
        if (!availableRoomIds.contains(request.getRoom_id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Room was booked by other");
        }

        if(request.getDiscount_code()!=null){

            List<DiscountEntity>discount= discountRepo.findByDiscountCode(request.getDiscount_code());
            if (discount.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid discount code");
            }

            if (discount==null) {
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid discount code");
            }
        }


        double baseCost=baseCostCal(roomType,startDate,endDate);

        // viết hàm riêng
        double promoDiscount=0;
        double durationDiscount=0;
        double vipDiscount=0;
        if(request.getDiscount_code() != null) {
            promoDiscount+=baseCost*discountRepo.getById(request.getDiscount_code()).getDiscountValue();
        }
        if(days==7){
            durationDiscount+= baseCost*0.2;
        } else if (days>3){
            durationDiscount+=baseCost*0.1;
        }
        if (request.is_VIP()) {
            vipDiscount+=baseCost*0.05;

        }

        double totalDiscount=promoDiscount+durationDiscount+vipDiscount;
        double totalCost=baseCost-totalDiscount;

        BookingEntity booking=new BookingEntity();
        booking.setHotelRoomEntity(hotelRoomRepo.findById(request.getRoom_id()));
        booking.setUserEntity(userRepo.getById(request.getUser_id()));
        booking.setBegin_date(startDate);
        booking.setEnd_date(endDate);
        if(request.getDiscount_code()!=null) {
            booking.setDiscountEntity(discountRepo.getReferenceById(request.getDiscount_code()));
        } // format
        booking.setTotalCost(totalCost);
        booking.setBookingStatus("occupied");
        booking.setCreatedAt(requestTimestamp);
        booking.setUpdatedAt(requestTimestamp);
        booking= bookingRepository.save(booking);
//        String bookingId = String.format("booking%03d", booking.getInternal_id()); // viết lại theo strategy
        booking= bookingRepository.save(booking);

        HotelRoomEntity room = booking.getHotelRoomEntity();
        room.setRoomStatus("occupied");
        room.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        hotelRoomRepo.save(room);

        Map<String, Object>details = new HashMap<>();
        details.put("base_cost",baseCost);
        details.put("promo_discount",promoDiscount);
        if (days==7) {
            details.put("discount_7_days",durationDiscount);
        } else if (days>3 && days<7) {
            details.put("discount_3_days",durationDiscount);
        }
        details.put("discount_vip",vipDiscount);

        Map<String, Object> response = new HashMap<>();
        response.put("booking_id",booking.getBooking_id());
        response.put("room_type", request.getRoom_type());
        response.put("total_cost", totalCost);
        response.put("status", "confirmed");
        response.put("details", details);
        response.put("message", "Đặt phòng thành công");
        return response;
    }


    private double baseCostCal( RoomTypeEntity roomTypeEntity, LocalDate startDate, LocalDate endDate) {
        double totalCost=0;
        List<LocalDate> holidays=holidayRepo.findAll().stream().map(h -> h.getHolidayDate())
                .toList();
        for(LocalDate date=startDate;date.isBefore(endDate);date = date.plusDays(1)){
            if(holidays.contains(date)) {
                totalCost+=roomTypeEntity.getHolidayPrice();
            } else if (date.getDayOfWeek().getValue()>=6) {
                totalCost+=roomTypeEntity.getWeekendsPrice();
            } else {
                totalCost+=roomTypeEntity.getNormalPrice();
            }
        }

        return totalCost;
    }


    @Override
    public Map<String, Object> cancelBooking(int booking_id, String cancel_timestamp) {
        OffsetDateTime cancelTime;
        try {
            cancelTime = OffsetDateTime.parse(cancel_timestamp);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid cancel_timestamp format, use ISO 8601");
        }

        BookingEntity booking = bookingRepository.findById(booking_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "booking_id not found"));

        OffsetDateTime startDateTime = booking.getBegin_date().atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        if (cancelTime.isAfter(now) || startDateTime.isEqual(now)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not cancel, time is out");
        }
        long HoursFromStart = ChronoUnit.HOURS.between(cancelTime, startDateTime);

        double totalCost = booking.getTotalCost();
        double refundAmount=0;
        double penalty=0;

        if(HoursFromStart>48) {
            refundAmount = totalCost;
            penalty = 0.0;
        } else if(HoursFromStart<48) {
            penalty = totalCost * 0.5;
            refundAmount = totalCost - penalty;
        }
        booking.setBookingStatus("cancelled");
        booking.setUpdatedAt(cancelTime);
        bookingRepository.save(booking);

        HotelRoomEntity room = booking.getHotelRoomEntity();
        if (room != null) {
            room.setRoomStatus("available");
            room.setUpdatedAt(cancelTime);
            hotelRoomRepo.save(room);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("booking_id", booking_id);
        response.put("refund_amount", refundAmount);
        response.put("penalty", penalty);
        response.put("message", HoursFromStart > 48 ? "Hủy thành công, hoàn tiền 100%" : "Hủy thành công, phạt 50%");

        return response;
    }

}
