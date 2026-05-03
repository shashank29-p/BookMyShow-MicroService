package com.example.booking.listners;

import com.example.booking.model.PaymentFailedEvent;
import com.example.booking.model.PaymentSuccessEvent;
import com.example.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventListener {

  private final BookingService bookingService;

  @KafkaListener(topics = "payment-success", groupId = "booking-group")
  public void handlePaymentSuccess(PaymentSuccessEvent event) {
    bookingService.confirmBooking(event.getBookingId());
  }

  @KafkaListener(topics = "payment-failed", groupId = "booking-group")
  public void handlePaymentFailed(PaymentFailedEvent event) {
    bookingService.cancelBooking(event.getBookingId());
  }
}