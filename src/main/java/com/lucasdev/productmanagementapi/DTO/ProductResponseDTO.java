package com.lucasdev.productmanagementapi.DTO;

import com.lucasdev.productmanagementapi.entities.Product;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;

    private Set<CategoryResponseDTO> categories = new HashSet<>();

    public ProductResponseDTO(Product product) {
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        imageUrl = product.getImageUrl();

        //using stream for take the Collection of my product class, and passing to a new collection of CategoryResponseDTO
        categories = product.getCategories().stream().map(CategoryResponseDTO::new).collect(Collectors.toSet());
    }
}
