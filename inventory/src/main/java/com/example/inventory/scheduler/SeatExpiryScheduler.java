package com.example.inventory.scheduler;

import com.example.inventory.repository.SeatRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SeatExpiryScheduler {

  private final SeatRepository seatRepository;

  @Scheduled(fixedRate = 60000)
  @Transactional
  public void releaseExpiredLocks() {
    seatRepository.releaseExpiredLocks(LocalDateTime.now());
  }
}