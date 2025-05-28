package com.lucasdev.productmanagementapi.DTO;

import com.lucasdev.productmanagementapi.entities.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CategoryResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public CategoryResponseDTO(Category category) {
        id = category.getId();
        name = category.getName();
    }
    //i take the Category attributes and pass to this class through the constructor
}
//this class show to the final user the only attributes of category that i want to show!