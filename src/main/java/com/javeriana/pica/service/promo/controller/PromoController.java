package com.javeriana.pica.service.promo.controller;

import com.javeriana.pica.service.promo.exception.ResourceNotFoundException;
import com.javeriana.pica.service.promo.model.Promo;
import com.javeriana.pica.service.promo.repository.PromoRepository;
import com.javeriana.pica.service.promo.repository.QueryProduct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
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
    public ResponseEntity<?> getPromoById(@PathVariable Long promoId) {
        HashMap<String, String> mapP = new HashMap<>();

        ResponseEntity<HashMap<String, String>> promo_r = promoRepository.findById(promoId)
                .map(promo -> {
                    mapP.put("id", promo.getId().toString());
                    mapP.put("name", promo.getName());
                    mapP.put("image_url", promo.getImage_url());
                    mapP.put("initial_date", promo.getInitial_date().toString());
                    mapP.put("end_date", promo.getEnd_date().toString());
                    mapP.put("description", promo.getDescription());
                    String producto = "";
                    QueryProduct queryProduct = new QueryProduct();
                    try {
                        producto = queryProduct.query_elastic(promo.getId_product());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mapP.put("producto", producto);
                    return new ResponseEntity<>(mapP, HttpStatus.OK);
                }).orElseThrow(() -> new ResourceNotFoundException("Promo not found with id " + promoId));;

        return new ResponseEntity<>(mapP, HttpStatus.OK);
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
