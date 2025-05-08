package com.jeongseok.pinkybrainbo.domain;

import com.jeongseok.pinkybrainbo.common.BaseEntity;
import com.jeongseok.pinkybrainbo.dto.product.ProductUpdateDto;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
	@OnDelete(action = OnDeleteAction.CASCADE) // 데이터베이스 레벨에서도 연관된 엔티티 삭제
	private List<ProductImage> productImages = new ArrayList<>();


	@Builder
	public Product(long id, String name, String category, String description, List<ProductImage> productImages) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.description = description;
		this.productImages = productImages;
	}

	public void updateProduct(ProductUpdateDto productUpdateDto) {
		this.name = productUpdateDto.getName();
		this.category = productUpdateDto.getCategory();
		this.description = productUpdateDto.getDescription();
	}

}
