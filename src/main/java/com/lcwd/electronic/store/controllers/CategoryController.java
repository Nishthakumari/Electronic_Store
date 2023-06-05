package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;


    //create
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid CategoryDto categoryDto)
    {
        //call service to save object
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> update(
            @RequestBody @Valid CategoryDto categoryDto,
            @PathVariable String categoryId) {

        CategoryDto updatedcategory = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updatedcategory, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(
            @PathVariable String categoryId
    )
    {
        categoryService.delete(categoryId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message("Category is deleted successfully").success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);


    }

    //get all

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String  sortDir
    ){
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);

    }

    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId)
    {
        CategoryDto categoryDto = categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    //create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable String categoryId,
            @RequestBody ProductDto productDto
    ){

        ProductDto savedProductDto = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);

    }

}
