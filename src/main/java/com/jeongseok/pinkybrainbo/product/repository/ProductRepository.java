package com.jeongseok.pinkybrainbo.product.repository;

import com.jeongseok.pinkybrainbo.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
