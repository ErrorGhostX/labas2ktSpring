package com.example.labs2kt.repository;

import com.example.labs2kt.model.Agreement;
import org.springframework.data.repository.CrudRepository;

public interface AgreementRepository extends CrudRepository<Agreement, Long> {
}
