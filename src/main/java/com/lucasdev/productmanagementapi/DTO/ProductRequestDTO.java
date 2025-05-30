package com.lucasdev.productmanagementapi.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "The field 'name' cannot be empty")
    private String name;

    @NotBlank(message = "The field 'description' cannot be empty")
    private String description;

    @NotNull(message = "the field 'price' cannot be null")
    @DecimalMin(value = "0.01", message = "the price must be greather than zero")
    private BigDecimal price;

    private String imageUrl;

    @NotEmpty(message = "the product need´s one or more categories!")
    private Set<Long> categoryIds = new HashSet<>();
    //list of categories IDS, in the hour of the user are create a product, their send an id for choice a category, and the program will be executed for see if the id already exists
}