package com.example.notification.event;

import com.example.notification.model.PaymentFailedEvent;
import com.example.notification.model.PaymentSuccessEvent;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationService notificationService;

  @KafkaListener(topics = "payment-success", groupId = "notification-group")
  public void handleSuccess(PaymentSuccessEvent event) {
    notificationService.sendSuccessNotification(event.getBookingId());
  }

  @KafkaListener(topics = "payment-failed", groupId = "notification-group")
  public void handleFailure(PaymentFailedEvent event) {
    notificationService.sendFailureNotification(event.getBookingId());
  }
}