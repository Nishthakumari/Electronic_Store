package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

public interface ProductService {

    //create
    ProductDto create(ProductDto productDto);

    //update
    ProductDto update(ProductDto productDto, String productId);

    //delete
    void delete(String productId);

    //get single
    ProductDto get(String productId);

    //get all
    PageableResponse<ProductDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir);

    //get all :live
    PageableResponse<ProductDto> getAllLive(int pageNo, int pageSize, String sortBy, String sortDir);

    //search product
    PageableResponse<ProductDto> searchByTitle(String query,  int pageNo, int pageSize, String sortBy, String sortDir);

   //create product with category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);

    //update category of product
    ProductDto updateCategory(String productId, String categoryId);

    PageableResponse<ProductDto> getAllofCategory(String categoryId, int pageNo, int pageSize, String sortBy, String sortDir);


    //other methods





}
