package com.example.labs2kt.repository;

import com.example.labs2kt.model.Bank;
import org.springframework.data.repository.CrudRepository;

public interface BankRepository extends CrudRepository<Bank, Long> {
}
