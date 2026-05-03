package com.example.notification.service;

import com.example.notification.model.Notification;
import com.example.notification.repository.NotificationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository repository;

  public void sendSuccessNotification(String bookingId) {

    String message = "Booking confirmed for ID: " + bookingId;

    logAndSave(bookingId, "SUCCESS", message);

    // future:
    // sendEmail()
    // sendSMS()
  }

  public void sendFailureNotification(String bookingId) {

    String message = "Booking failed for ID: " + bookingId;

    logAndSave(bookingId, "FAILED", message);
  }

  private void logAndSave(String bookingId, String type, String message) {

    System.out.println("NOTIFICATION: " + message);

    Notification notification = new Notification();
    notification.setBookingId(bookingId);
    notification.setType(type);
    notification.setMessage(message);
    notification.setCreatedAt(LocalDateTime.now());

    repository.save(notification);
  }
}