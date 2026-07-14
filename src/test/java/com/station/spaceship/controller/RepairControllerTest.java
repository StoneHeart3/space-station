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
