package com.lucasdev.productmanagementapi.repositories;

import com.lucasdev.productmanagementapi.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // a personalized method for search by productName and categoryName with query
    @Query("SELECT DISTINCT p FROM Product p JOIN p.categories c " +
            "WHERE (:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
            "AND (:categoryName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%')))")
    Page<Product> findByProductAndCategoryNames(@Param("productName") String productName, @Param("categoryName") String categoryName, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.categories WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(Long id);

    //findALL
    @Query(value = "SELECT DISTINCT p FROM Product p JOIN FETCH p.categories",
            countQuery = "SELECT COUNT(DISTINCT p) FROM Product p JOIN p.categories")
    Page<Product> findAllProductsWithCategories(Pageable pageable);

}
//the repository do the connection with the database, and with the services layer...important understand this