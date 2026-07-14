package com.station.spaceship.controller;

import com.station.spaceship.model.Spaceship;
import com.station.spaceship.repository.SpaceshipRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ships")
public class SpaceshipController {

    private final SpaceshipRepository repository;

    public SpaceshipController(SpaceshipRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/register")
    public Spaceship registerShip(@RequestBody Spaceship ship) {
        return repository.save(ship);
    }

    @GetMapping
    public List<Spaceship> getAllShips() {
        return repository.findAll();
    }
}
