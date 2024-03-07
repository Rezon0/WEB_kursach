package com.example.russionTest.Services;

import com.example.russionTest.domain.Records;
import com.example.russionTest.repos.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    private final RecordRepo appointmentRepository;

    @Autowired
    public RecordService(RecordRepo appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void saveRecord(Records appointment) {
        appointmentRepository.save(appointment);
    }
}

