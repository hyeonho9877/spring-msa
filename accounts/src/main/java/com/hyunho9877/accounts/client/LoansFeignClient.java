package com.hyunho9877.accounts.client;

import com.hyunho9877.accounts.model.Customer;
import com.hyunho9877.accounts.model.Loans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("loans")
public interface LoansFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "myLoans", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Loans> getLoansDetail(@RequestHeader(name = "eazybank-correlation-id") String correlationId, @RequestBody Customer customer);
}
