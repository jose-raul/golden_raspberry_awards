package com.desafio.outsera.service;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AwardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CsvImportService csvImportService;
    
    @Test
    void testGetProducerWithMaxMinAwardIntervals_MultipleWinners() throws Exception {
        InputStream csvInputStream = getClass().getResourceAsStream("/test-data-multiple-winners.csv");
        csvImportService.importCsv(csvInputStream);

        mockMvc.perform(get("/award/intervals"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.min", hasSize(1)))
        .andExpect(jsonPath("$.max", hasSize(2)))
        .andExpect(jsonPath("$.min[0].producer", is("Producer 1")))
        .andExpect(jsonPath("$.min[0].interval", is(1)))
        .andExpect(jsonPath("$.min[0].previousWin", is(2000)))
        .andExpect(jsonPath("$.min[0].followingWin", is(2001)))
        .andExpect(jsonPath("$.max[0].producer", is("Producer 2")))
        .andExpect(jsonPath("$.max[0].interval", is(5)))
        .andExpect(jsonPath("$.max[0].previousWin", is(2005)))
        .andExpect(jsonPath("$.max[0].followingWin", is(2010)))
        .andExpect(jsonPath("$.max[1].producer", is("Producer 2")))
        .andExpect(jsonPath("$.max[1].interval", is(5)))
        .andExpect(jsonPath("$.max[1].previousWin", is(2010)))
        .andExpect(jsonPath("$.max[1].followingWin", is(2015)));
    }

    @Test
    void testGetProducerWithMaxMinAwardIntervals_SingleWinner() throws Exception {
        InputStream csvInputStream = getClass().getResourceAsStream("/test-data-single-winner.csv");
        csvImportService.importCsv(csvInputStream);

        mockMvc.perform(get("/award/intervals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.min", hasSize(0)))
            .andExpect(jsonPath("$.max", hasSize(0)));
    }

    @Test
    void testGetProducerWithMaxMinAwardIntervals_SameInterval() throws Exception {
        InputStream csvInputStream = getClass().getResourceAsStream("/test-data-same-interval.csv");
        csvImportService.importCsv(csvInputStream);

        mockMvc.perform(get("/award/intervals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.min", hasSize(3)))
            .andExpect(jsonPath("$.max", hasSize(3)))
            .andExpect(jsonPath("$.min[*].interval", everyItem(is(1))))
            .andExpect(jsonPath("$.max[*].interval", everyItem(is(1))));
    }
    
    @Test
    void testGetProducerWithMaxMinAwardIntervals_VaryingIntervals() throws Exception {
    	InputStream csvInputStream = getClass().getResourceAsStream("/test-data-varying-intervals.csv");
        csvImportService.importCsv(csvInputStream);

        mockMvc.perform(get("/award/intervals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.min", hasSize(2)))
            .andExpect(jsonPath("$.max", hasSize(1)))
            .andExpect(jsonPath("$.min[0].producer", is("Producer 1")))
            .andExpect(jsonPath("$.min[0].interval", is(2)))
            .andExpect(jsonPath("$.min[0].previousWin", is(1990)))
            .andExpect(jsonPath("$.min[0].followingWin", is(1992)))
            .andExpect(jsonPath("$.min[1].producer", is("Producer 1")))
            .andExpect(jsonPath("$.min[1].interval", is(2)))
            .andExpect(jsonPath("$.min[1].previousWin", is(1992)))
            .andExpect(jsonPath("$.min[1].followingWin", is(1994)))
            .andExpect(jsonPath("$.max[0].producer", is("Producer 2")))
            .andExpect(jsonPath("$.max[0].interval", is(10)))
            .andExpect(jsonPath("$.max[0].previousWin", is(2005)))
            .andExpect(jsonPath("$.max[0].followingWin", is(2015)));
    }
    
    @Test
    void testGetProducerWithMaxMinAwardIntervals_NoWinners() throws Exception {
    	InputStream csvInputStream = getClass().getResourceAsStream("/test-data-no-winners.csv");

        csvImportService.importCsv(csvInputStream);
        
        mockMvc.perform(get("/award/intervals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.min", hasSize(0)))
            .andExpect(jsonPath("$.max", hasSize(0)));
    }

}

