package com.shabushabu.javashop.products.services;

import com.shabushabu.javashop.products.model.Product;

import java.math.BigDecimal;
import java.util.*;

public class ProductService {

    private Map<String, Product> fakeProductDAO = new HashMap<>();

    //Define product categories
    static final String CATEGORY_AA = "Action-Adventure";
    static final String CATEGORY_RPG = "RPG";
    static final String CATEGORY_SPORTS = "Sports";
    static final String CATEGORY_SIM = "Simulation";

    public ProductService() {
        fakeProductDAO.put("1", new Product("19237", "Invisible Time", CATEGORY_AA, new BigDecimal(49.99)));
        fakeProductDAO.put("2", new Product("12382", "Buttercup's Day Out", CATEGORY_AA, new BigDecimal(49.99)));
        fakeProductDAO.put("3", new Product("11826", "Antispace", CATEGORY_AA, new BigDecimal(29.99)));
        fakeProductDAO.put("4", new Product("11342", "Cloudrush", CATEGORY_AA, new BigDecimal(59.99)));
        fakeProductDAO.put("5", new Product("12422", "Farcrest", CATEGORY_AA, new BigDecimal(49.99)));
        fakeProductDAO.put("6", new Product("23122", "Phantoms of Magic", CATEGORY_RPG, new BigDecimal(19.99)));
        fakeProductDAO.put("7", new Product("25641", "Castlevale", CATEGORY_RPG, new BigDecimal(16.99)));
        fakeProductDAO.put("8", new Product("28910", "Thunder Awakening", CATEGORY_RPG, new BigDecimal(39.99)));
        fakeProductDAO.put("9", new Product("31213", "Snowpros", CATEGORY_SPORTS, new BigDecimal(29.99)));
        fakeProductDAO.put("10", new Product("32833", "Extreme Chess IX", CATEGORY_SPORTS, new BigDecimal(54.99)));
        fakeProductDAO.put("11", new Product("42917", "SRE Simulator", CATEGORY_SIM, new BigDecimal(54.99)));
        fakeProductDAO.put("12", new Product("48291", "Kubernetes Tycoon", CATEGORY_SIM, new BigDecimal(24.99)));
        fakeProductDAO.put("13", new Product("41827", "Helicopter Pilot", CATEGORY_SIM, new BigDecimal(34.99)));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(fakeProductDAO.values());
    }

    public Optional<Product> getProduct(String id) {
        return Optional.ofNullable(fakeProductDAO.get(id));
    }
}
