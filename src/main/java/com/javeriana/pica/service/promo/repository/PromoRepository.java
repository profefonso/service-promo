package com.javeriana.pica.service.promo.repository;

import com.javeriana.pica.service.promo.model.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PromoRepository extends JpaRepository<Promo, Long>{

}
