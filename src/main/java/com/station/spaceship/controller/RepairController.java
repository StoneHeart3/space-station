package com.station.spaceship.controller;

import com.station.spaceship.model.RepairTicket;
import com.station.spaceship.repository.RepairTicketRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repairs")
public class RepairController {

    private final RepairTicketRepository repository;

    public RepairController(RepairTicketRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/tickets")
    public RepairTicket createTicket(@RequestBody RepairTicket ticket) {
        ticket.setStatus("PENDING");
        return repository.save(ticket);
    }

    @GetMapping("/tickets")
    public List<RepairTicket> getAllTickets() {
        return repository.findAll();
    }
}
