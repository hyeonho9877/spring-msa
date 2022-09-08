package com.hyunho9877.cards.controller;

import com.hyunho9877.cards.config.CardsServiceConfig;
import com.hyunho9877.cards.model.Cards;
import com.hyunho9877.cards.model.Customer;
import com.hyunho9877.cards.model.Properties;
import com.hyunho9877.cards.repository.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardsController {

    private static final Logger logger = LoggerFactory.getLogger(CardsController.class);
    private final CardsRepository cardsRepository;
    private final CardsServiceConfig cardsConfig;

    @PostMapping("/myCards")
    public List<Cards> getCardDetails(@RequestHeader("eazybank-correlation-id") String correlationId, @RequestBody Customer customer) {
        logger.info("CardsController.getCardDetails started");
        List<Cards> cards = cardsRepository.findByCustomerId(customer.getCustomerId());
        logger.info("CardsController.getCardDetails ended");
        return cards;
    }

    @GetMapping("/cards/properties")
    public ResponseEntity<Properties> getPropertyDetails() {
        return ResponseEntity.ok(new Properties(cardsConfig.getMsg(), cardsConfig.getBuildVersion(), cardsConfig.getMailDetails(), cardsConfig.getActiveBranches()));
    }
}
