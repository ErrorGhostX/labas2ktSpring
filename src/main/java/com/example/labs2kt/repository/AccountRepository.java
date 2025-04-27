package com.example.labs2kt.repository;

import com.example.labs2kt.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
