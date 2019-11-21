package com.javeriana.pica.service.promo.controller;

import com.javeriana.pica.service.promo.exception.ResourceNotFoundException;
import com.javeriana.pica.service.promo.model.Promo;
import com.javeriana.pica.service.promo.repository.PromoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class PromoController {

    @Autowired
    private PromoRepository promoRepository;

    @GetMapping("/promos")
    public Page<Promo> getQuestions(Pageable pageable) {
        return promoRepository.findAll(pageable);
    }

    @PostMapping("/promos")
    public Promo createQuestion(@Valid @RequestBody Promo promo) {
        return promoRepository.save(promo);
    }

    @GetMapping("/promos/{promoId}")
    public Optional<Promo> getPromoById(@PathVariable Long promoId) {
        return promoRepository.findById(promoId);
    }

    @PutMapping("/promos/{promoId}")
    public Promo updatePromo(@PathVariable Long promoId,
                                   @Valid @RequestBody Promo promoRequest) {
        return promoRepository.findById(promoId)
                .map(promo -> {
                    promo.setName(promoRequest.getName());
                    promo.setImage_url(promoRequest.getImage_url());
                    promo.setId_product(promoRequest.getId_product());
                    promo.setInitial_date(promoRequest.getInitial_date());
                    promo.setEnd_date(promoRequest.getEnd_date());
                    promo.setDescription(promoRequest.getDescription());
                    promo.setEstate(true);
                    return promoRepository.save(promo);
                }).orElseThrow(() -> new ResourceNotFoundException("Promo not found with id " + promoId));
    }

    @DeleteMapping("/promos/{promoId}")
    public Promo deletePromo(@PathVariable Long promoId) {
        return promoRepository.findById(promoId)
                .map(promo -> {
                    promo.setEstate(false);
                    return promoRepository.save(promo);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + promoId));
    }
}
