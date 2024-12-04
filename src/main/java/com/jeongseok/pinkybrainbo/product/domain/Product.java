package com.jeongseok.pinkybrainbo.product.domain;

import com.jeongseok.pinkybrainbo.common.BaseEntity;
import com.jeongseok.pinkybrainbo.product_image.domain.ProductImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "category")
	private String category;

	@Column(name = "description")
	private String description;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductImage> productImages = new ArrayList<>();

	@Builder
	public Product(long id, String name, String category, String description, List<ProductImage> productImages) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.description = description;
		this.productImages = productImages;
	}

	// 상품 업데이트 메소드 필요
	public void update(Product product) {
		this.name = product.getName();
		this.description = product.getDescription();
		this.productImages = product.getProductImages();
	}

	public void addProductImage(List<ProductImage> productImages) {
		for (ProductImage productImage : productImages) {
			productImage.setProduct(this); // 연관 관계 주인 설정
		}

		this.productImages = productImages;
	}

}
