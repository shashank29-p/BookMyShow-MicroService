package com.example.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {

  @Id
  private String bookingId;

  private Double amount;

  private String status;
  // INITIATED, SUCCESS, FAILED

  private String transactionId;
}
