package com.javeriana.pica.service.promo.controller;

import com.javeriana.pica.service.promo.exception.ResourceNotFoundException;
import com.javeriana.pica.service.promo.model.Promo;
import com.javeriana.pica.service.promo.repository.PromoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

import com.javeriana.pica.service.promo.security.Validate_Token;

@RestController
public class PromoController {

    @Autowired
    private PromoRepository promoRepository;

    @GetMapping("/promos")
    public Page<Promo> getQuestions(Pageable pageable) {

        return promoRepository.findAll(pageable);
    }

    @PostMapping("/promos")
    public ResponseEntity<?> createQuestion(
            @Valid @RequestBody Promo promo,
            @RequestHeader (name="Authorization") String token
            )
            throws Exception {
        Validate_Token validate_token = new Validate_Token();
        if(validate_token.send_token(token)){
            promoRepository.save(promo);
            return new ResponseEntity<>(promo, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("{'error': 'UNAUTHORIZED'}", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/promos/{promoId}")
    public Optional<Promo> getPromoById(@PathVariable Long promoId) {
        return promoRepository.findById(promoId);
    }

    @PutMapping("/promos/{promoId}")
    public ResponseEntity<?>  updatePromo(@PathVariable Long promoId,
                             @Valid @RequestBody Promo promoRequest,
                             @RequestHeader (name="Authorization") String token
    ) throws Exception{
        Validate_Token validate_token = new Validate_Token();
        if(validate_token.send_token(token)){
            Promo promo_r = promoRepository.findById(promoId)
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
            return new ResponseEntity<>(promo_r, HttpStatus.OK);
        }
        return new ResponseEntity<>("{error: UNAUTHORIZED}", HttpStatus.UNAUTHORIZED);

    }

    @DeleteMapping("/promos/{promoId}")
    public ResponseEntity<?> deletePromo(
            @PathVariable Long promoId,
            @RequestHeader (name="Authorization") String token
    ) throws Exception {
        Validate_Token validate_token = new Validate_Token();
        if (validate_token.send_token(token)) {
            Promo promo_r = promoRepository.findById(promoId)
                    .map(promo -> {
                        promo.setEstate(false);
                        return promoRepository.save(promo);
                    }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + promoId));

            return new ResponseEntity<>(promo_r, HttpStatus.OK);
        }
        return new ResponseEntity<>("{error: UNAUTHORIZED}", HttpStatus.UNAUTHORIZED);
    }

}
