package com.hyunho9877.accounts.client;

import com.hyunho9877.accounts.model.Cards;
import com.hyunho9877.accounts.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("cards")
public interface CardsFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "myCards", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Cards> getCardDetail(@RequestHeader(name = "eazybank-correlation-id") String correlationId, @RequestBody Customer customer);
}
