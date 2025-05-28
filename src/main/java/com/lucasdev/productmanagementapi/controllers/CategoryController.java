package com.lucasdev.productmanagementapi.controllers;

import com.lucasdev.productmanagementapi.DTO.CategoryRequestDTO;
import com.lucasdev.productmanagementapi.DTO.CategoryResponseDTO;
import com.lucasdev.productmanagementapi.DTO.StandardMessageDTO;
import com.lucasdev.productmanagementapi.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;

@RestController
@RequestMapping(value = "/categories")
@Tag(name = "Categories", description = "Endpoints for managing product categories")
public class CategoryController {

    //here i can use FINAL using the injection via constructor instead of using the @Autowired
    private final CategoryService categoryService;

    //dependency injection via constructor
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //the path of my endpoint
    @GetMapping(value = "/{id}")
    //customing my swagger for a good documentation!
    @Operation(summary = "Get category by ID",
            description = "Retrieve a single category by its unique identifier",
            responses = {
                    //i'm taking the two main HTTP status... 200/404
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved category",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found, for more details enter in https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status/404",
                            //a little gift here to know more details about the error
                            content = @Content)
            })
    public ResponseEntity<CategoryResponseDTO> findById( @PathVariable Long id) {
        CategoryResponseDTO dto = categoryService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    //new endpoint for list all categories with pagination
    @GetMapping
    //call the swagger
    @Operation(summary = "List all categories", description = "Get a paged list of all product categories", responses = {@ApiResponse(responseCode = "200", description = "List recovered with successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),  //the return is a page!

            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error.",
                    content = @Content)
    })
    public ResponseEntity<Page<CategoryResponseDTO>> findAllPaged(@PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable){
        Page<CategoryResponseDTO> list = categoryService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    //endpoint for insert a category in the program
    @PostMapping
    @Operation(summary = "Insert a category", description = "Insert a category in the program, with the necessary attributes", responses = {
            //the main HTTP errors codes that can happen
            @ApiResponse(responseCode =  "201", description = "Category inserted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, some value already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error.",
                    content = @Content)
    })
    public ResponseEntity<CategoryResponseDTO> insert(@Valid @RequestBody CategoryRequestDTO dto) {

        CategoryResponseDTO newDto = categoryService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();

        //return 201 Created if the method it´s okay
        return ResponseEntity.created(uri).body(newDto);
    }

    //endpoint for update data of category!
    @PutMapping(value = "/{id}")
    //this method is  similar to insert
    @Operation(summary = "Update the category", description = "Update the category switching the field 'name'.", responses = { @ApiResponse(responseCode =  "200", description = "Category updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, some value already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error.",
                    content = @Content)
    })
    //puting here the PathVariable first for good practiques
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO dto) {

        //the difference is, that here we needed put the id of category of we want to change
        CategoryResponseDTO newDto = categoryService.update(id, dto);

        //return the code 200... OK !
        return ResponseEntity.ok().body(newDto);
    }

    //the last endpoint... delete!
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a category.", description = "Delete one category by ID!", responses = {
            @ApiResponse(responseCode =  "200", description = "Category deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Integrity error", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error.",
                    content = @Content)
    })
    public ResponseEntity<StandardMessageDTO> delete(@PathVariable Long id) {
        categoryService.delete(id);

        String message = "Category ID: " +id +" was deleted successfully";
        //taking the current path
        String path = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath();
        HttpStatus status = HttpStatus.OK;
        //turning more explicit the NO_CONTENT but the code will stay 200 because the return is ok

        StandardMessageDTO dto = new StandardMessageDTO(Instant.now(), status.name(), message, path);

        //return ok code 200... generally this return a no content code 204, but i want to show a personalized message for the final user
        return ResponseEntity.ok().body(dto);
    }
}
