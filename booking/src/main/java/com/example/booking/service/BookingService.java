package com.example.booking.service;

import com.example.booking.dto.BookingRequest;
import com.example.booking.model.Booking;
import com.example.booking.model.BookingCreatedEvent;
import com.example.booking.repository.BookingRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

  private final BookingRepository repository;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public String createBooking(BookingRequest request) {

    String bookingId = UUID.randomUUID().toString();

    Booking booking = new Booking();
    booking.setBookingId(bookingId);
    booking.setShowId(request.getShowId());
    booking.setSeatIds(String.join(",", request.getSeatIds()));
    booking.setAmount(request.getAmount());
    booking.setStatus("PENDING");

    repository.save(booking);

    BookingCreatedEvent event = new BookingCreatedEvent(
        bookingId,
        request.getShowId(),
        request.getSeatIds(),
        request.getAmount()
    );

    kafkaTemplate.send("booking-created",bookingId, event);

    return bookingId;
  }

  public void confirmBooking(String bookingId) {
    Booking booking = repository.findById(bookingId).orElseThrow();
    booking.setStatus("CONFIRMED");
    repository.save(booking);
  }

  public void cancelBooking(String bookingId) {
    Booking booking = repository.findById(bookingId).orElseThrow();
    booking.setStatus("CANCELLED");
    repository.save(booking);
  }
}