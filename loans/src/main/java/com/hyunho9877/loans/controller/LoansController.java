package com.hyunho9877.loans.controller;

import com.hyunho9877.loans.model.Customer;
import com.hyunho9877.loans.model.Loans;
import com.hyunho9877.loans.repository.LoansRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoansController {

    private final LoansRepository loansRepository;

    @PostMapping("/myLoans")
    public List<Loans> getLoansDetail(@RequestBody Customer customer) {
        return loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
    }

}
