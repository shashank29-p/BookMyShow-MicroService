package com.example.inventory.service;

import com.example.inventory.model.BookingCreatedEvent;
import com.example.inventory.model.PaymentFailedEvent;
import com.example.inventory.model.Seat;
import com.example.inventory.model.SeatsLockedEvent;
import com.example.inventory.repository.SeatRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final SeatRepository seatRepository;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Transactional
  public void lockSeats(BookingCreatedEvent event) {

    List<Seat> seats = seatRepository.findBySeatIdIn(event.getSeatIds());

    // 🔥 Validate all seats available
    boolean allAvailable = seats.stream()
        .allMatch(seat -> "AVAILABLE".equals(seat.getStatus()));

    if (!allAvailable) {
      kafkaTemplate.send("payment-failed",
          new PaymentFailedEvent(event.getBookingId()));
      return;
    }

    // 🔥 Lock seats
    LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

    for (Seat seat : seats) {
      seat.setStatus("LOCKED");
      seat.setLockedBy(event.getBookingId());
      seat.setLockExpiry(expiry);
    }

    seatRepository.saveAll(seats);

    kafkaTemplate.send("seats-locked",
        new SeatsLockedEvent(event.getBookingId()));
  }

  @Transactional
  public void confirmSeats(String bookingId) {

    List<Seat> seats = seatRepository.findAll().stream()
        .filter(seat -> bookingId.equals(seat.getLockedBy()))
        .toList();

    for (Seat seat : seats) {
      seat.setStatus("BOOKED");
      seat.setLockExpiry(null);
    }

    seatRepository.saveAll(seats);
  }

  @Transactional
  public void releaseSeats(String bookingId) {

    List<Seat> seats = seatRepository.findAll().stream()
        .filter(seat -> bookingId.equals(seat.getLockedBy()))
        .toList();

    for (Seat seat : seats) {
      seat.setStatus("AVAILABLE");
      seat.setLockedBy(null);
      seat.setLockExpiry(null);
    }

    seatRepository.saveAll(seats);
  }
}
