package com.thief.repository;

import com.thief.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    List<Account> findAllByActiveTrue();

    Optional<Account> findByIdAndActiveTrue(String id);
}
