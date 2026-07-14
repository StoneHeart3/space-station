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
