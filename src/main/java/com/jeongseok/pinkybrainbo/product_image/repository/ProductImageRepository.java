package com.jeongseok.pinkybrainbo.product_image.repository;

import com.jeongseok.pinkybrainbo.product_image.domain.ProductImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

	Optional<List<ProductImage>> findByProduct_Id(long id);
}
