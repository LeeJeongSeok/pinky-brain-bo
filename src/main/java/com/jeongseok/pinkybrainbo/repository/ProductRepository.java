package com.jeongseok.pinkybrainbo.repository;

import com.jeongseok.pinkybrainbo.domain.product.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select p from Product p join fetch p.productImages where (p.name like %?1%)")
	List<Product> findProductBySearchKeyword(String searchKeyword);

}
