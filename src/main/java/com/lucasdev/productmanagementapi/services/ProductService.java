package com.lucasdev.productmanagementapi.services;

import com.lucasdev.productmanagementapi.DTO.ProductQuotationResponseDTO;
import com.lucasdev.productmanagementapi.DTO.ProductRequestDTO;
import com.lucasdev.productmanagementapi.DTO.ProductResponseDTO;
import com.lucasdev.productmanagementapi.entities.Category;
import com.lucasdev.productmanagementapi.entities.Product;
import com.lucasdev.productmanagementapi.exceptions.ResourceNotFoundException;
import com.lucasdev.productmanagementapi.integrations.ExchangeRateApiClient;
import com.lucasdev.productmanagementapi.repositories.CategoryRepository;
import com.lucasdev.productmanagementapi.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class ProductService {

    //this class will be have three dependencies
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ExchangeRateApiClient exchangeRateApiClient;

    //injected dependencies via constructor
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ExchangeRateApiClient exchangeRateApiClient) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.exchangeRateApiClient = exchangeRateApiClient;
    }

    //makes a transactional, but only read.
    @Transactional(readOnly = true)
    public ProductResponseDTO findProductById(Long id) {
        //throw a exception if the user put a invalid ID!
        Product product = productRepository.findByIdWithCategory(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id " + id));
        //return a new instance of dto receiving a entity product, respecting the layers
        return new ProductResponseDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAllPaged(String productName, String categoryName, Pageable pageable) {

        Page<Product> entityPage = productRepository.findByProductAndCategoryNames(productName, categoryName, pageable);

        // create a new instance of dto at each iteration of map
        return entityPage.map(e -> new ProductResponseDTO(e));
    }

    //NEW METHOD FOR NOT PROBLEM WITH SQL QUERIES IN HOUR OF SEARCH
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAllSimplePaged(Pageable pageable) {

        Page<Product> entitiesPage = productRepository.findAllProductsWithCategories(pageable);

        return entitiesPage.map(p -> new ProductResponseDTO(p));
    }

    @Transactional
    public ProductResponseDTO insertProduct(ProductRequestDTO dtoRef) {
        try {
            //new instance of Product
            Product entity = new Product();

            //copying the properties ignoring the id, because is the same
            BeanUtils.copyProperties(dtoRef, entity, "id");

            //auxiliar method for take the category of product
            associateCategoriesToProduct(entity, dtoRef.getCategoryIds());

            //save the ENTITY in the repository... respect the layers
            entity = productRepository.save(entity);

            //the DTO i´ll show to the final user!
            return new ProductResponseDTO(entity);
        }catch(DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("one or more fields in the product are causing integrity error", e);
        }
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dtoRef) {

        try {
            Product entity = productRepository.findByIdWithCategory(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

            //pass the droRef properties to entity
            BeanUtils.copyProperties(dtoRef, entity, "id");

            associateCategoriesToProduct(entity, dtoRef.getCategoryIds());

            entity = productRepository.save(entity);

            return new ProductResponseDTO(entity);
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("one or more fields in the product are causing integrity error", e);
        }
    }

    public void associateCategoriesToProduct(Product entity, Set<Long> categoryIds) {
        //clear the list first
        entity.getCategories().clear();

        if(categoryIds != null && !categoryIds.isEmpty()) {

            for (Long cats : categoryIds) {
                //taking the category from repository if exists, or i throw my exception
                Category category = categoryRepository.findById(cats).orElseThrow(()-> new ResourceNotFoundException("Category not found with id " + cats + " operation failed"));
                entity.getCategories().add(category); //add the category to the entity list
            }
        }
    }

    @Transactional(readOnly = true)
    public ProductQuotationResponseDTO getProductPriceInOtherCurrency(Long productId, String targetCurrencyCode){

        //take the product from repository
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found with id " + productId));

        //TURNING MORE EXPLICT THE CURRENCY BASE!
        String BRL = "BRL";

        //the operation for doing the conversion
        BigDecimal conversionRate = exchangeRateApiClient.getConversionRate(BRL, targetCurrencyCode.toUpperCase());

        BigDecimal originalPriceBRL = product.getPrice();

        BigDecimal convertedPrice = originalPriceBRL.multiply(conversionRate);


        //new instance of ProductQuatation for return to the program
        ProductQuotationResponseDTO newDto = new ProductQuotationResponseDTO(
                product.getId(),
                product.getName(),
                originalPriceBRL,
                convertedPrice,
                targetCurrencyCode.toUpperCase() // the final user can se the name of currency code more friendly
        );
        return newDto;  //or return direct in instanciation, but this way becomes clearly
    }

    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id " + productId);
        }
        productRepository.deleteById(productId);
    }
}
