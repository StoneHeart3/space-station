package com.station.spaceship.repository;

import com.station.spaceship.model.RepairTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairTicketRepository extends JpaRepository<RepairTicket, Long> {
}
