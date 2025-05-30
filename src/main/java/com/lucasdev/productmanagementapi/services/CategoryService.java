package com.lucasdev.productmanagementapi.services;

import com.lucasdev.productmanagementapi.DTO.CategoryRequestDTO;
import com.lucasdev.productmanagementapi.DTO.CategoryResponseDTO;
import com.lucasdev.productmanagementapi.entities.Category;
import com.lucasdev.productmanagementapi.exceptions.DataBaseException;
import com.lucasdev.productmanagementapi.exceptions.IntegrityDataException;
import com.lucasdev.productmanagementapi.exceptions.ResourceNotFoundException;
import com.lucasdev.productmanagementapi.repositories.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryService {

    //starts to doing a dependency with a constructor
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //method for find the category for id
    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id){
        try {
            //if categoryRepository back´s empty, i'll throw my personalized exception!
            Category entity = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
            //doing the new instance
            CategoryResponseDTO responseDTO = new CategoryResponseDTO(entity);
            //returning the DTO!
            return responseDTO;
        }catch (DataIntegrityViolationException e){
            throw new DataBaseException("data with invalid format entered");
        }
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> findAllPaged(Pageable pageable){

        //calling the findAll of repository, this returns a Page<Category>, and after i'll have to transform in a CategoryResponseDTO
        Page<Category> entitiesPage = categoryRepository.findAll(pageable);

        //here i transform the entity in a dto, with lamdba expression!
        return entitiesPage.map(e -> new CategoryResponseDTO(e));
    }

    //makes transactional for respects the ACID principles
    @Transactional
    public CategoryResponseDTO insert(CategoryRequestDTO dto){

        try {
            Category entity = new Category(); //new instance of category

            BeanUtils.copyProperties(dto, entity, "id");
            //copy the properties and ignore the ID

            //save the entity on the repository
            entity = categoryRepository.save(entity);

            //return the dto
            return new CategoryResponseDTO(entity);
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Conflict... the name " +dto.getName() + " already exists");
        }
    }

    //always respecting the ACID principle...
    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto){

        try {
            Category entity = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
            //treating the case of category´s back empty throwing my custom exception here

            BeanUtils.copyProperties(dto, entity, "id");
            entity = categoryRepository.save(entity);
            return new CategoryResponseDTO(entity);

        }catch (DataIntegrityViolationException e){
            //throwing exception for invalid data
            throw new DataIntegrityViolationException("Category name already exists or data integrity violation.", e);
        }
    }

    @Transactional
    public void delete(Long id){
        try {
            if (!categoryRepository.existsById(id)) {
                throw new ResourceNotFoundException("Category not found with id " + id);
            }
            categoryRepository.deleteById(id);

        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }
}