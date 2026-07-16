package com.station.spaceship.controller;

import com.station.spaceship.model.Spaceship;
import com.station.spaceship.repository.SpaceshipRepository;
import org.springframework.cache.annotation.Cacheable;
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

    @GetMapping("/{id}/telemetry")
    @Cacheable(value = "telemetryCache", key = "#id")
    public String getShipTelemetry(@PathVariable Long id) {
        System.out.println("⚠️ CACHE MISS: Querying heavy database for ship " + id + "...");
        // Simulate a slow 3-second database lookup/calculation
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Ship " + id + " Status: Shields 100%, Reactor Stable.";
   }
}
