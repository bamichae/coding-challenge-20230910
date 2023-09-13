package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CompensationServiceImpl implements CompensationService {
    /**
     * Holds the compensation logic
     */

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation for employee [{}]", compensation.getEmployee());

        // Throw error if the Employee is not in the system
        if (employeeRepository.findByEmployeeId(compensation.getEmployee()) == null) {
            throw new RuntimeException("Invalid employeeId: " + compensation.getEmployee());
        }

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Optional<Compensation> read(String employeeId) {
        LOG.debug("Reading compensation for employee [{}]", employeeId);

        return compensationRepository.findById(employeeId);
    }
}
