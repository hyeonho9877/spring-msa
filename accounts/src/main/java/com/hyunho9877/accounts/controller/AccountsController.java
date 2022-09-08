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
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountsController {

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
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


//    @Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
    @PostMapping("/myCustomerDetails")
    @CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
    public CustomerDetails myCustomerDetails(@RequestHeader(name = "eazybank-correlation-id") String correlationId, @RequestBody Customer customer) {
        logger.info("AccountsController.myCustomerDetails started");
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow();
        List<Loans> loans = loansClient.getLoansDetail(correlationId, customer);
        List<Cards> cards = cardsClient.getCardDetail(correlationId, customer);
        logger.info("AccountsController.myCustomerDetails ended");
        return new CustomerDetails(accounts, loans, cards);
    }

    private CustomerDetails myCustomerDetailsFallBack(@RequestHeader(name = "eazybank-correlation-id") String correlationId, Customer customer, Throwable t) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow();
        List<Loans> loans = loansClient.getLoansDetail(correlationId, customer);

        return new CustomerDetails(accounts, loans, null);
    }

    @GetMapping("/sayHello")
    @RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
    public String sayHello() {
        return "Hello, Welcome to Eazy Bank";
    }


    private String sayHelloFallback(Throwable throwable) {
        return "Hi, Welcome to Eazy Bank";
    }
}
