package com.shabushabu.javashop.products.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Product {
    private String id;
    private String name;
    private String category;
    private BigDecimal price;

    public Product() {
        // Needed for Jackson deserialization
    }

    public Product(String id, String name, String category, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getCategory() {
        return category;
    }

    @JsonProperty
    public BigDecimal getPrice() {
        return price;
    }
}
