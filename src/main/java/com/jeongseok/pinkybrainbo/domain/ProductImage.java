package com.jeongseok.pinkybrainbo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "product_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE) // 부모 엔티티 삭제 시 함께 삭제
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "image_order")
	private int imageOrder;

	@Builder
	public ProductImage(long id, Product product, String imageUrl, int imageOrder) {
		this.id = id;
		this.product = product;
		this.imageUrl = imageUrl;
		this.imageOrder = imageOrder;
	}

	public void addProduct(Product product) {
		this.product = product;
		product.getProductImages().add(this);
	}

}
