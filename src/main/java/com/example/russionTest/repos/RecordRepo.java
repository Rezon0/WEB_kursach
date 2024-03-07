package com.example.russionTest.repos;

import com.example.russionTest.domain.Records;
import org.springframework.data.repository.CrudRepository;

public interface RecordRepo extends CrudRepository<Records, Integer> {

}