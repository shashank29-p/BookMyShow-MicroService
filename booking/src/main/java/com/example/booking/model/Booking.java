package com.example.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

  @Id
  private String bookingId;

  private String showId;

  private String seatIds; // comma-separated

  private Double amount;

  private String status; // PENDING, CONFIRMED, CANCELLED
}