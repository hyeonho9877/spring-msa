package com.hyunho9877.accounts.repository;

import com.hyunho9877.accounts.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByCustomerId(int customerId);
}
