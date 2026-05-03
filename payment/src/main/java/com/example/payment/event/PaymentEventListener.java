package com.example.payment.event;

import com.example.payment.model.SeatsLockedEvent;
import com.example.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

  private final PaymentService paymentService;

  @KafkaListener(topics = "seats-locked", groupId = "payment-group")
  public void handleSeatsLocked(SeatsLockedEvent event) {
    paymentService.processPayment(event);
  }
}
