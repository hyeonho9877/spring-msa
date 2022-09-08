package com.hyunho9877.loans.controller;

import com.hyunho9877.loans.config.LoansServiceConfig;
import com.hyunho9877.loans.model.Customer;
import com.hyunho9877.loans.model.Loans;
import com.hyunho9877.loans.model.Properties;
import com.hyunho9877.loans.repository.LoansRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoansController {

    private final static Logger logger = LoggerFactory.getLogger(LoansController.class);
    private final LoansRepository loansRepository;
    private final LoansServiceConfig loansConfig;

    @PostMapping("/myLoans")
    public List<Loans> getLoansDetail(@RequestHeader("eazybank-correlation-id") String correlationId, @RequestBody Customer customer) {
        logger.info("LoansController.getLoansDetail started");
        List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
        logger.info("LoansController.getLoansDetail ended");
        return loans;
    }

    @GetMapping("/loans/properties")
    public ResponseEntity<Properties> getPropertyDetails() {
        return ResponseEntity.ok(new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(), loansConfig.getMailDetails(), loansConfig.getActiveBranches()));
    }


}
