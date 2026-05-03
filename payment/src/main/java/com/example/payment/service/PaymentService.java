package com.example.payment.service;

import com.example.payment.model.Payment;
import com.example.payment.model.PaymentFailedEvent;
import com.example.payment.model.PaymentSuccessEvent;
import com.example.payment.model.SeatsLockedEvent;
import com.example.payment.repository.PaymentRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository repository;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Transactional
  public void processPayment(SeatsLockedEvent event) {

    // 🔥 Idempotency check
    Optional<Payment> existing = repository.findById(event.getBookingId());

    if (existing.isPresent()) {
      Payment payment = existing.get();

      if ("SUCCESS".equals(payment.getStatus())) {
        kafkaTemplate.send(
            "payment-success",
            new PaymentSuccessEvent(event.getBookingId())
        );
        return;
      }

      if ("FAILED".equals(payment.getStatus())) {
        kafkaTemplate.send("payment-failed",
            new PaymentFailedEvent(event.getBookingId()));
        return;
      }
    }

    // 🔥 Create new payment record
    Payment payment = new Payment();
    payment.setBookingId(event.getBookingId());
    payment.setAmount(100.0); // dummy amount
    payment.setStatus("INITIATED");

    repository.save(payment);

    // 🔥 Simulate payment
    boolean success = simulatePayment();

    if (success) {
      payment.setStatus("SUCCESS");
      payment.setTransactionId(UUID.randomUUID().toString());
      repository.save(payment);

      kafkaTemplate.send(
          "payment-success",
          new PaymentSuccessEvent(event.getBookingId())
      );

    } else {
      payment.setStatus("FAILED");
      repository.save(payment);

      kafkaTemplate.send("payment-failed",
          new PaymentFailedEvent(event.getBookingId()));
    }
  }

  private boolean simulatePayment() {
    return Math.random() > 0.3; // 70% success
  }
}