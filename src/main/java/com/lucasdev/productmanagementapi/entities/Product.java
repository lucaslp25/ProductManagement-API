package com.lucasdev.productmanagementapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150) //the max of length name
    @NotBlank(message = "The field 'name' cannot be empty")
    private String name;

    @Column(length = 255)
    @NotBlank(message = "The field 'description' cannot be empty")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "the field 'price' cannot be null")
    @DecimalMin(value = "0.01", message = "the price must be greather than zero")
    private BigDecimal price;
    //using BigDecimal for precision in monetaries values

    //this field stay optional...
    private String ImageUrl;

    @ManyToMany
    @JoinTable(name = "tb_product_category", //table name
            joinColumns = @JoinColumn(name = "product_id"),  //columns names
            inverseJoinColumns = @JoinColumn(name = "category_id"
    ))
    private Set<Category> categories = new HashSet<>();

    //HashCode and Equals only for ID!!
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
