package com.lucasdev.productmanagementapi.controllers;

import com.lucasdev.productmanagementapi.DTO.*;
import com.lucasdev.productmanagementapi.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    //dependency via constructor!
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //first endpoint of productController
    @GetMapping(value = "/{id}")
    //documentation of swagger
    @Operation(summary = "Get the product by id", description = "Find the product in the repository putting your unique identifier (id)", responses = {
            //if product finded show the code 200
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDTO.class))),
            //if bad request show code 400 in documentation... etc
            @ApiResponse(responseCode = "400", description = "Bad request enter", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error occurred.", content = @Content)
    })
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable("id") Long id) {
        //get the dto from the service layer
        ProductResponseDTO dto = productService.findProductById(id);
        //return to the final user the 'ok' (code 200),  and dto with (body)!
        return ResponseEntity.ok().body(dto);
    }

    //now the endpoint of findAllPaged (can search by filters)
    @GetMapping
    @Operation(summary = "List all products", description = "Get a paginated list of all products with optional filters by product name and category name.",parameters = {
            // now i put parameters for a better documentation!
            @Parameter(name = "productName", description = "Filter by product name", required = false),
            @Parameter(name = "categoryName", description = "Filter by category name", required = false),
            // Pageable parameters are usually automatically documented by Springdoc, but explicit is fine.
            //details of documentation in max level
            @Parameter(name = "page", description = "Page number (0 - indexed)", required = false, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Number of elements per page", required = false, schema = @Schema(type = "integer", defaultValue = "10")), //default of swagger of itens per page
            @Parameter(name = "sort", description = "Sorting criteria in the format:  Default sort is 'name'.", required = false, schema = @Schema(type = "string", defaultValue = "name" ))

    }, responses = {
            @ApiResponse(responseCode = "200", description = "List recovered with successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            //the return is a page!
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error.", content = @Content)
    })
    public ResponseEntity<Page<ProductResponseDTO>> findAllPaged(@RequestParam(name = "productName", required = false) String productName, @RequestParam(name = "categoryName", required = false) String categoryName, @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        //parameters for this method is the name of product and the category for do the filter, but not´s obrigatory to put

        //doing the filter, the parameters have the required = false, so help in this work
        Page<ProductResponseDTO> list = productService.findAllPaged(productName, categoryName, pageable);

        //return ok 200.. and the content!
        return ResponseEntity.ok().body(list);
    }

    //endpoint of simple paged
    @GetMapping(value = "/all")
    @Operation(summary = "List all products (simple paginated)", description = "Get a paginated list of all products without filters.", parameters = {
            //Usually Here´s default messages for a good documentation in Swagger
            @Parameter(name = "page", description = "Page number (0 - indexed)", required = false, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Number of elements per page", required = false, schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(name = "sort", description = "Sorting criteria in the format:(asc|desc). Default sort is 'name'.", required = false, schema = @Schema(type = "string", defaultValue = "name", example = "name,asc"))
    },responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error.", content = @Content)
    })
    public ResponseEntity<Page<ProductResponseDTO>> findAllSimplePaged(
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        Page<ProductResponseDTO> list = productService.findAllSimplePaged(pageable);

        //return ok 200
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    @Operation(summary = "Insert a product", description = "insert a product into the repository through by product´s data", responses = {
            @ApiResponse(responseCode = "201", description = "Product inserted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, some value already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error.",
                    content = @Content)
    })          //@valid here... if invalid enter for users, the responsible layer is GlobalHndler
    public ResponseEntity<ProductResponseDTO> insert(@Valid @RequestBody ProductRequestDTO dto) {

        ProductResponseDTO newDto = productService.insertProduct(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(newDto.getId()).toUri();

        //return created... code 201!
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a product", description = "Update the product switching the product´s data fields", responses = {
            @ApiResponse(responseCode = "200", description = "Product update successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Resource not found..", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, some value already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error.",
                    content = @Content)
    })                                          //valid here too
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        //learning that, if all right in the program in this layer not call the entity... only the dtos!
        ProductResponseDTO newDto = productService.updateProduct(id, dto);
        //return the ok cod 200
        return ResponseEntity.ok().body(newDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a product.", description = "Delete product by id!", responses = {
            @ApiResponse(responseCode =  "200", description = "Product deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Integrity error", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error, unexpected error.",
                    content = @Content)
    })
    public ResponseEntity<StandardMessageDTO> deleteById(@PathVariable Long id) {
        //deleting the product
        productService.deleteProduct(id);
        //doing the personalized message
        String message = "Product ID: " +id +" was deleted successfully";
        HttpStatus status = HttpStatus.OK;
        //status ok for this message, the normal for delete is 204 NO_CONTENT

        //the path of uri
        String path = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath();

        //the instance of my standard message
        StandardMessageDTO messageDTO = new StandardMessageDTO(Instant.now(),status.name(), message, path);

        return ResponseEntity.ok().body(messageDTO);
    }

    //the last endpoint of class, this go convert the price of product for other´s currencies
    @GetMapping(value = "/quotation/{productId}")
    @Operation(summary = "Get a product price in oter currency", description = "Retrieve the price of a product converted to a specified currency using an external API.", parameters = {
            @Parameter(name = "productId", description = "Unique identifier of the product.", required = true), // path parameter
            @Parameter(name = "currencyCode", description = "The 3 letter code of the target currency, example: USD, EUR).", required = true, schema = @Schema(type = "string", example = "USD"))
            //query parameter
    },responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved conversion", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductQuotationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request error, invalids data enter.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server erro, unexpected error", content = @Content),
            //A http code for unavailable service... util for working with external API´S!
            @ApiResponse(responseCode = "503", description = "Service Unavailable error", content = @Content)
    })
    public ResponseEntity<ProductQuotationResponseDTO> getConvertedPrice(@PathVariable Long productId, @RequestParam(name = "currencyCode") String currencyCode) {

        //using here my auxiliar method in service layer that get the product id and the currency code that´ll go convert (the default currency of products is BRL)
        ProductQuotationResponseDTO dtoConvert = productService.getProductPriceInOtherCurrency(productId, currencyCode);

        //return the code ok()... code 200!
        return ResponseEntity.ok().body(dtoConvert);
    }
}