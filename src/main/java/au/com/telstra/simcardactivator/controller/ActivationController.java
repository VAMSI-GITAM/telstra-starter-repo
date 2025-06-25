package au.com.telstra.simcardactivator.controller;

import au.com.telstra.simcardactivator.model.ActivationRequest;
import au.com.telstra.simcardactivator.model.ActivationRecord;
import au.com.telstra.simcardactivator.service.ActivationService;
import au.com.telstra.simcardactivator.repository.ActivationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/sim")
public class ActivationController {

    @Autowired
    private ActivationService activationService;

    @Autowired
    private ActivationRecordRepository repository;

    @PostMapping("/activate")
    public ResponseEntity<String> activateSim(@RequestBody ActivationRequest request) {
        boolean result = activationService.activateSim(request);
        return result
                ? ResponseEntity.ok("✅ SIM activation successful.")
                : ResponseEntity.status(500).body("❌ SIM activation failed.");
    }

    @GetMapping("/status")
    public ResponseEntity<?> getActivationStatus(@RequestParam Long simCardId) {
        return repository.findById(simCardId)
                .map(record -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("iccid", record.getIccid());
                    result.put("customerEmail", record.getCustomerEmail());
                    result.put("active", record.isActive());
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
