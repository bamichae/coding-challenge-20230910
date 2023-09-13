package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CompensationController {
    /**
     * REST controller responsible for inserting and reading employee compensation information.
     */

    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping("/compensation")
    public ResponseEntity<?> create(@RequestBody Compensation compensation) {
        LOG.debug("Received Compensation create request for employee [{}]", compensation.getEmployee());

        // Attempt to create compensation information
        Compensation createdCompensation = compensationService.create(compensation);
        if (createdCompensation == null) {
            LOG.debug("Compensation information not created for [{}]", compensation.getEmployee());
            String message = "Compensation not created for employee: " + compensation.getEmployee();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        } else {
            return ResponseEntity.ok(createdCompensation);
        }
    }

    @GetMapping("/compensation/{employeeId}")
    public ResponseEntity<?> read(@PathVariable String employeeId) {
        LOG.debug("Received Compensation create request for employee [{}]", employeeId);

        Optional<Compensation> compensation = compensationService.read(employeeId);

        if (compensation.isPresent()) {
            return ResponseEntity.ok(compensation.get());
        } else {
            LOG.debug("Could not read compensation information for employee [{}]", employeeId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Compensation not found for employeeId: " + employeeId);
        }
    }
}
