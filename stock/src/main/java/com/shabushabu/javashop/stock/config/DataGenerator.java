package com.shabushabu.javashop.stock.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.shabushabu.javashop.stock.model.Stock;
import com.shabushabu.javashop.stock.repositories.StockRepository;

import javax.annotation.PostConstruct;

@Component
public class DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

    private StockRepository stockRepository;

    @Autowired
    protected DataGenerator(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    @Transactional
    public void init() {
        LOGGER.info("Generating synthetic data for demonstration purposes...");

        stockRepository.save(new Stock("19237", "12345678", 65));
        stockRepository.save(new Stock("12382", "34567890", 22));
        stockRepository.save(new Stock("11826", "54236745", 92));
        stockRepository.save(new Stock("11342", "25847614", 0));
        stockRepository.save(new Stock("12422", "53156388", 1));
        stockRepository.save(new Stock("23122", "46345678", 54));
        stockRepository.save(new Stock("25641", "24567890", 21));
        stockRepository.save(new Stock("28910", "53823745", 32));
        stockRepository.save(new Stock("31213", "28747614", 45));
        stockRepository.save(new Stock("32833", "57156388", 64));
        stockRepository.save(new Stock("42917", "59226745", 51));
        stockRepository.save(new Stock("48291", "57347614", 65));
        stockRepository.save(new Stock("41827", "37156388", 24));

        LOGGER.info("... data generation complete");
    }
}
