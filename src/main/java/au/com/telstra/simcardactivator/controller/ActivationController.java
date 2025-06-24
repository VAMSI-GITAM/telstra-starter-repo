package au.com.telstra.simcardactivator.controller;

import au.com.telstra.simcardactivator.model.ActivationRequest;
import au.com.telstra.simcardactivator.service.ActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sim")
public class ActivationController {

    @Autowired
    private ActivationService activationService;

    @PostMapping("/activate")
    public ResponseEntity<String> activateSim(@RequestBody ActivationRequest request) {
        boolean result = activationService.activateSim(request);

        if (result) {
            return ResponseEntity.ok("✅ SIM activation successful.");
        } else {
            return ResponseEntity.status(500).body("❌ SIM activation failed.");
        }
    }
}
