package com.jeongseok.pinkybrainbo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

	@GetMapping("/products")
	public String getProducts(Model model) {
		return "/product/list";
	}

	@GetMapping("/products/new")
	public String createProduct() {
		return "/product/create";
	}

	@GetMapping("/product/{id}")
	public String getProduct(@PathVariable("id") String id) {
		return "/product/detail";
	}
}
