package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private int cartItemId;
    private Product product;
    private int quantity;
    private int totalPrice;


}
