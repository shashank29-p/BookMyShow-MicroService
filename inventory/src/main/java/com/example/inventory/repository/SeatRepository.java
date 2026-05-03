package com.example.inventory.repository;

import com.example.inventory.model.Seat;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<Seat, String> {

  List<Seat> findBySeatIdIn(List<String> seatIds);

  @Modifying
  @Query("update Seat s set s.status='AVAILABLE', s.lockedBy=null, s.lockExpiry=null where s.lockExpiry < :now")
  void releaseExpiredLocks(LocalDateTime now);
}
