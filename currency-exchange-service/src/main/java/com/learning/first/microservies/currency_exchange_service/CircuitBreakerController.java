package com.learning.first.microservies.currency_exchange_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.retry.RetryException;

@RestController

public class CircuitBreakerController {

    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @Retryable(
        value = { RetryException.class }, // Exception types to retry on
        maxAttempts = 3, // Number of retry attempts
        backoff = @Backoff(delay = 2000) // Delay between retries
    )
    @GetMapping("/sample-api")
    public String sampleApi() {
        logger.info("Sample API received");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/random", String.class);
        return forEntity.getBody();
    }

    @Recover
    public String hardCodedResponse(RetryException ex) {
        logger.error("Fallback method executed due to exception: ", ex);
        return "Fallback response";
    }
}

