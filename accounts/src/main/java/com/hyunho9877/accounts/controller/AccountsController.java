package com.hyunho9877.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hyunho9877.accounts.client.CardsFeignClient;
import com.hyunho9877.accounts.client.LoansFeignClient;
import com.hyunho9877.accounts.config.AccountsServiceConfig;
import com.hyunho9877.accounts.model.*;
import com.hyunho9877.accounts.repository.AccountsRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountsController {

    private final AccountsRepository accountsRepository;
    private final AccountsServiceConfig accountsConfig;
    private final CardsFeignClient cardsClient;
    private final LoansFeignClient loansClient;

    @PostMapping("/myAccount")
    public Accounts getAccountDetails(@RequestBody Customer customer) {
        return accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow();
    }

    @GetMapping("/accounts/properties")
    public ResponseEntity<Properties> getPropertyDetails() throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ResponseEntity.ok(new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(), accountsConfig.getMailDetails(), accountsConfig.getActiveBranches()));
    }

    @CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
    @PostMapping("/myCustomerDetails")
    public CustomerDetails myCustomerDetails(@RequestBody Customer customer) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow();
        List<Loans> loans = loansClient.getLoansDetail(customer);
        List<Cards> cards = cardsClient.getCardDetail(customer);

        return new CustomerDetails(accounts, loans, cards);
    }

    private CustomerDetails myCustomerDetailsFallBack(Customer customer, Throwable t) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow();
        List<Loans> loans = loansClient.getLoansDetail(customer);

        return new CustomerDetails(accounts, loans, null);
    }
}
