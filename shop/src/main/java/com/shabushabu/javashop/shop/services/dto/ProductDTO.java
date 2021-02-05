package com.shabushabu.javashop.shop.services.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private String id;
    private String name;
    private String category;
    private BigDecimal price;

    public ProductDTO() {
    }

    public ProductDTO(String id, String name, String category, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
