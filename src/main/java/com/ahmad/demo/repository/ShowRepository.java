package com.ahmad.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ahmad.demo.model.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {
    
    Show findByShowNumber(int showNumber);
    
    @Modifying
    @Query("UPDATE Show s SET s.availableSeats = :availableSeats WHERE s.showNumber = :showNumber")
    int updateAvailableSeatsByShowNumber(@Param("showNumber") int showNumber, @Param("availableSeats") List<String> availableSeats);
}