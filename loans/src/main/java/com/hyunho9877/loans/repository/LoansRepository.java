package com.hyunho9877.loans.repository;

import com.hyunho9877.loans.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoansRepository extends JpaRepository<Loans, Long> {
    List<Loans> findByCustomerIdOrderByStartDtDesc(int customerId);
}
