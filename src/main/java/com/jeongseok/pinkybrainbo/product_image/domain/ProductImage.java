package com.jeongseok.pinkybrainbo.product_image.domain;

import com.jeongseok.pinkybrainbo.common.BaseEntity;
import com.jeongseok.pinkybrainbo.product.domain.Product;
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

@Getter
@Entity
@Table(name = "product_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
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

}
