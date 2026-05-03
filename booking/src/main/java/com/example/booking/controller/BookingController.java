package com.example.booking.controller;

import com.example.booking.dto.BookingRequest;
import com.example.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

  private final BookingService bookingService;

  @PostMapping
  public ResponseEntity<String> createBooking(@RequestBody BookingRequest request) {
    String bookingId = bookingService.createBooking(request);
    return ResponseEntity.ok(bookingId);
  }
}