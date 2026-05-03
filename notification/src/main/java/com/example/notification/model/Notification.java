package com.example.notification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "notifications")
@Data
public class Notification {

  @Id
  @GeneratedValue
  private Long id;

  private String bookingId;

  private String type;
  // SUCCESS / FAILED

  private String message;

  private LocalDateTime createdAt;
}
