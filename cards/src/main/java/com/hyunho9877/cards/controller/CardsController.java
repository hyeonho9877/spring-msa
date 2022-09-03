package com.hyunho9877.cards.controller;

import com.hyunho9877.cards.model.Cards;
import com.hyunho9877.cards.model.Customer;
import com.hyunho9877.cards.repository.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardsController {

    private final CardsRepository cardsRepository;

    @PostMapping("/myCards")
    public List<Cards> getCardDetails(@RequestBody Customer customer) {
        return cardsRepository.findByCustomerId(customer.getCustomerId());
    }
}
