package au.com.telstra.simcardactivator.service;

import au.com.telstra.simcardactivator.model.ActivationRequest;
import au.com.telstra.simcardactivator.model.ActuatorResponse;
import au.com.telstra.simcardactivator.model.ActivationRecord;
import au.com.telstra.simcardactivator.repository.ActivationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ActivationService {

    private static final String ACTUATOR_URL = "http://localhost:8444/actuate";

    @Autowired
    private ActivationRecordRepository repository;

    public boolean activateSim(ActivationRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare JSON payload
        String payload = "{\"iccid\":\"" + request.getIccid() + "\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        boolean success = false;

        try {
            ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(
                    ACTUATOR_URL, entity, ActuatorResponse.class
            );
            success = response.getBody() != null && response.getBody().isSuccess();

        } catch (Exception e) {
            System.out.println("Error while calling actuator: " + e.getMessage());
        }

        // Save activation result to DB
        ActivationRecord record = new ActivationRecord(
                request.getIccid(),
                request.getCustomerEmail(),
                success
        );
        repository.save(record);

        return success;
    }
}
