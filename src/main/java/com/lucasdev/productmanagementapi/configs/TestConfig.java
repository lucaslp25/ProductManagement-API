package com.lucasdev.productmanagementapi.configs;

import com.lucasdev.productmanagementapi.entities.Category;
import com.lucasdev.productmanagementapi.entities.Product;
import com.lucasdev.productmanagementapi.repositories.CategoryRepository;
import com.lucasdev.productmanagementapi.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class TestConfig implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    public TestConfig(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        //populating my database with categories for

        productRepository.deleteAll(); //delete all before tests
        categoryRepository.deleteAll();

        Category c1 = new Category(null, "Eletronics");
        Category c2 = new Category(null, "Apparel & Accessories");
        Category c3 = new Category(null, "Pet Supplies");
        Category c4 = new Category(null, "Home");
        Category c5 = new Category(null, "Books");
        Category c6 = new Category(null, "Games");
        Category c7 = new Category(null, "Health");
        Category c8 = new Category(null, "Toys");
        Category c9 = new Category(null, "Food");

        List<Category> savedCategory = categoryRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5,c6, c7, c8, c9));

        Set<Category> p1Cat = new HashSet<>();
        Set<Category> p2Cat = new HashSet<>();
        Set<Category> p3Cat = new HashSet<>();
        Set<Category> p4Cat = new HashSet<>();
        Set<Category> p5Cat = new HashSet<>();
        Set<Category> p6Cat = new HashSet<>();

        Product p1 = new Product(null, "Computer", "A new inovation computer, with the last technologies ",new BigDecimal("540.00"), null); //eletronic

        Product p2 = new Product(null, "X-BURGUER", "A Delicious X-burguer",new BigDecimal("20.99"), null); //food

        Product p3 = new Product(null, "Buzz Lightyear", "A toy for childrens play",new BigDecimal("48.10"), null); //toys

        Product p4 = new Product(null, "Organic Dog Food", "Premium dry food made with natural ingredients for dogs.", new BigDecimal("70.00"), null); //pets

        Product p5 = new Product(null, "Smart Home Speaker", "Voice-controlled smart speaker with premium sound and integrated virtual assistant", new BigDecimal("129.99"), null); //eletronics, home

        Product p6 = new Product(null, "Pet Feeder", "Programmable pet food dispenser with portion control and meal scheduling", new BigDecimal("85.99"), null); //eletronics, home, pet


        if (savedCategory.get(0) != null) {
            p1Cat.add(savedCategory.get(0));
        }
        if (savedCategory.get(8) != null) {
            p2Cat.add(savedCategory.get(8));
        }
        if (savedCategory.get(7) != null) {
            p3Cat.add(savedCategory.get(7));
        }
        if (savedCategory.get(2) != null){
            p4Cat.add(savedCategory.get(2));
        }
        if (savedCategory.get(0) != null && savedCategory.get(3) != null) {
            p5Cat.add(savedCategory.get(0));
            p5Cat.add(savedCategory.get(3));
        }
        if(savedCategory.get(0) != null && savedCategory.get(2) != null && savedCategory.get(3) !=null){
            p6Cat.add(savedCategory.get(0));
            p6Cat.add(savedCategory.get(3));
            p6Cat.add(savedCategory.get(2));
        }

        p1.setCategories(p1Cat);
        p2.setCategories(p2Cat);
        p3.setCategories(p3Cat);
        p4.setCategories(p4Cat);
        p5.setCategories(p5Cat);
        p6.setCategories(p6Cat);

        productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6));

    }
}