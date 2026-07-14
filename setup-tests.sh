#!/bin/bash
echo "Initiating Test Suite Build for Spaceship Station..."

# 1. Create Test Directory Structure
mkdir -p src/test/java/com/station/spaceship/controller

# 2. Create SpaceshipControllerTest
cat << 'EOF' > src/test/java/com/station/spaceship/controller/SpaceshipControllerTest.java
package com.station.spaceship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.station.spaceship.model.Spaceship;
import com.station.spaceship.repository.SpaceshipRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpaceshipController.class)
public class SpaceshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpaceshipRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterShip() throws Exception {
        Spaceship ship = new Spaceship();
        ship.setId(1L);
        ship.setModel("Cruiser Class");
        ship.setCaptainName("guru_prasath");
        ship.setFuelLevel(95);

        Mockito.when(repository.save(any(Spaceship.class))).thenReturn(ship);

        mockMvc.perform(post("/api/v1/ships/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ship)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.captainName").value("guru_prasath"))
                .andExpect(jsonPath("$.model").value("Cruiser Class"));
    }

    @Test
    public void testGetAllShips() throws Exception {
        Spaceship ship1 = new Spaceship();
        ship1.setCaptainName("guru_prasath");
        
        Spaceship ship2 = new Spaceship();
        ship2.setCaptainName("Solo");

        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(ship1, ship2));

        mockMvc.perform(get("/api/v1/ships")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].captainName").value("guru_prasath"));
    }
}
EOF

# 3. Create RepairControllerTest
cat << 'EOF' > src/test/java/com/station/spaceship/controller/RepairControllerTest.java
package com.station.spaceship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.station.spaceship.model.RepairTicket;
import com.station.spaceship.repository.RepairTicketRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepairController.class)
public class RepairControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepairTicketRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTicket() throws Exception {
        RepairTicket ticket = new RepairTicket();
        ticket.setId(100L);
        ticket.setShipId(1L);
        ticket.setIssueDescription("Hyperdrive alignment failing");
        ticket.setStatus("PENDING");

        Mockito.when(repository.save(any(RepairTicket.class))).thenReturn(ticket);

        mockMvc.perform(post("/api/v1/repairs/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.issueDescription").value("Hyperdrive alignment failing"));
    }

    @Test
    public void testGetAllTickets() throws Exception {
        RepairTicket ticket = new RepairTicket();
        ticket.setId(100L);
        ticket.setStatus("IN_PROGRESS");

        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(ticket));

        mockMvc.perform(get("/api/v1/repairs/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));
    }
}
EOF

echo "Test suite generated successfully!"
