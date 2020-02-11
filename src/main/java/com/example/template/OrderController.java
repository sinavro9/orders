package com.example.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

    private static final String RESPONSE_STRING_FORMAT = "order => %s\n";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RestTemplate restTemplate;

    @Value("${api.url.delivery:http://delivery:8080}")
    private String remoteURL;

    @PostMapping("/order")
    ResponseEntity<String> fakeOrder(@RequestBody String data) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(remoteURL + "/startDelivery", data, String.class);
            String response = responseEntity.getBody();

            logger.info(String.format(RESPONSE_STRING_FORMAT, response.trim()));

            return ResponseEntity.ok(String.format(RESPONSE_STRING_FORMAT, response.trim()));
        } catch (Exception ex) {
            logger.warn("Exception trying to get the response from order service.", ex);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(String.format(RESPONSE_STRING_FORMAT, ex.getMessage()));
        }
    }
}
