package com.example.inventory.model;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "seats")
@Data
public class Seat {

  @Id
  private String seatId;

  private String showId;

  private String status;
  // AVAILABLE, LOCKED, BOOKED

  private String lockedBy; // bookingId

  private LocalDateTime lockExpiry;
}