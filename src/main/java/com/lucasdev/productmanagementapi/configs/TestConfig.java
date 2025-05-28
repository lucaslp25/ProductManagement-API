package com.lucasdev.productmanagementapi.configs;

import com.lucasdev.productmanagementapi.entities.Category;
import com.lucasdev.productmanagementapi.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class TestConfig implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public TestConfig(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        //populating my database with categories for test

        Category c1 = new Category(null, "Eletronics");
        Category c2 = new Category(null, "Clothing");
        Category c3 = new Category(null, "Health");
        Category c4 = new Category(null, "Beauty");
        Category c5 = new Category(null, "Toys");

        categoryRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));
    }
}
