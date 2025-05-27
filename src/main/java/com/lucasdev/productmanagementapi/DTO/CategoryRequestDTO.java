package com.lucasdev.productmanagementapi.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "The field 'name' cannot be empty")
    private String name;
}
