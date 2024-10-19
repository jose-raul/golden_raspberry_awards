package com.desafio.outsera.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.server.LocalServerPort;


import com.desafio.outsera.dto.AwardIntervalDTO;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AwardControllerIntegrationTest {

    private static final String HOST = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAwardReturnMinAndMaxOnBody() {
    	ParameterizedTypeReference<Map<String, List<AwardIntervalDTO>>> responseType =
    	        new ParameterizedTypeReference<Map<String, List<AwardIntervalDTO>>>() {};
    	        
    	        
    	ResponseEntity<Map<String, List<AwardIntervalDTO>>> responseEntity = restTemplate.exchange(
    	                HOST + port + "/award/intervals",
    	                HttpMethod.GET,
    	                null,
    	                responseType
    	        );
    	
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().get("max"));
        assertNotNull(responseEntity.getBody().get("min"));            
    }
    
    @Test
    void testAwardReturnMinAndMaxRangeFromDefaultFile() {
        ParameterizedTypeReference<Map<String, List<AwardIntervalDTO>>> responseType =
                new ParameterizedTypeReference<Map<String, List<AwardIntervalDTO>>>() {};

        ResponseEntity<Map<String, List<AwardIntervalDTO>>> responseEntity = restTemplate.exchange(
                HOST + port + "/award/intervals",
                HttpMethod.GET,
                null,
                responseType
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().get("max"));
        assertNotNull(responseEntity.getBody().get("min"));

        List<AwardIntervalDTO> minIntervals = responseEntity.getBody().get("min");
        assertEquals(1, minIntervals.size());
        AwardIntervalDTO minInterval = minIntervals.get(0);
        assertEquals("Joel Silver", minInterval.producer());
        assertEquals(1, minInterval.interval());
        assertEquals(1990, minInterval.previousWin());
        assertEquals(1991, minInterval.followingWin());

        List<AwardIntervalDTO> maxIntervals = responseEntity.getBody().get("max");
        assertEquals(1, maxIntervals.size());
        AwardIntervalDTO maxInterval = maxIntervals.get(0);
        assertEquals("Matthew Vaughn", maxInterval.producer());
        assertEquals(13, maxInterval.interval());
        assertEquals(2002, maxInterval.previousWin());
        assertEquals(2015, maxInterval.followingWin());
    }
    

}

