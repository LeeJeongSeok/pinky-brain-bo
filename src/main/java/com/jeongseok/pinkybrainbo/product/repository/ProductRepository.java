package com.jeongseok.pinkybrainbo.product.repository;

import com.jeongseok.pinkybrainbo.product.domain.Product;
import com.jeongseok.pinkybrainbo.product_image.domain.ProductImage;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select p from Product p where (p.name like %?1%)")
	List<Product> findProductBySearchKeyword(String searchKeyword);

}
