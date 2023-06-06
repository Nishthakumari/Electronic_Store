package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {

    //add item to cart
    //case1: cart for user is not available. We will create the cart and then add the item to it.
    //case2: cart available, add the item to the cart.

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    //remove all item from cart
    void clearCart(String userId);


}
