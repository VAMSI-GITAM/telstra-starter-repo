package au.com.telstra.simcardactivator.service;

import au.com.telstra.simcardactivator.model.ActivationRequest;
import au.com.telstra.simcardactivator.model.ActuatorResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ActivationService {

    private final String ACTUATOR_URL = "http://localhost:8444/actuate";

    public boolean activateSim(ActivationRequest request) {
        // Create a RestTemplate to make HTTP request
        RestTemplate restTemplate = new RestTemplate();

        // Prepare JSON payload with just the ICCID
        String payload = "{\"iccid\":\"" + request.getIccid() + "\"}";

        // Set headers for JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Wrap the payload and headers into an HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        try {
            // Make POST request to actuator service
            ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(
                    ACTUATOR_URL, entity, ActuatorResponse.class);

            // Return true if activation was successful
            return response.getBody() != null && response.getBody().isSuccess();

        } catch (Exception e) {
            System.out.println("Error while calling actuator: " + e.getMessage());
            return false;
        }
    }
}
