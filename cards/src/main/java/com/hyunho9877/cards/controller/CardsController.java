package com.hyunho9877.cards.controller;

import com.hyunho9877.cards.config.CardsServiceConfig;
import com.hyunho9877.cards.model.Cards;
import com.hyunho9877.cards.model.Customer;
import com.hyunho9877.cards.model.Properties;
import com.hyunho9877.cards.repository.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardsController {

    private final CardsRepository cardsRepository;
    private final CardsServiceConfig cardsConfig;

    @PostMapping("/myCards")
    public List<Cards> getCardDetails(@RequestBody Customer customer) {
        return cardsRepository.findByCustomerId(customer.getCustomerId());
    }

    @GetMapping("/cards/properties")
    public ResponseEntity<Properties> getPropertyDetails() {
        return ResponseEntity.ok(new Properties(cardsConfig.getMsg(), cardsConfig.getBuildVersion(), cardsConfig.getMailDetails(), cardsConfig.getActiveBranches()));
    }
}
