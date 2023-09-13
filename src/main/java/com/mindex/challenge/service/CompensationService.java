package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

import java.util.Optional;

public interface CompensationService {
    Compensation create(Compensation compensation);

    Optional<Compensation> read(String employeeId);
}
