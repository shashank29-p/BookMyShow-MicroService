package com.example.inventory.listners;

import com.example.inventory.model.BookingCreatedEvent;
import com.example.inventory.model.PaymentFailedEvent;
import com.example.inventory.model.PaymentSuccessEvent;
import com.example.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventListener {

  private final InventoryService inventoryService;

  @KafkaListener(topics = "booking-created", groupId = "inventory-group")
  public void handleBookingCreated(BookingCreatedEvent event) {
    inventoryService.lockSeats(event);
  }

  @KafkaListener(topics = "payment-success", groupId = "inventory-group")
  public void handlePaymentSuccess(PaymentSuccessEvent event) {
    inventoryService.confirmSeats(event.getBookingId());
  }

  @KafkaListener(topics = "payment-failed", groupId = "inventory-group")
  public void handlePaymentFailed(PaymentFailedEvent event) {
    inventoryService.releaseSeats(event.getBookingId());
  }
}