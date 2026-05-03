package com.example.booking.dto;

import java.util.List;
import lombok.Data;

@Data
public class BookingRequest {
  private String showId;
  private List<String> seatIds;
  private Double amount;
}