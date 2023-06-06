package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CartServiceImpl implements CartService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {


        int quantity = request.getQuantity();
        String  productId = request.getProductId();

        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product with this id not found!!"));

        //fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found !!"));

        cartRepository.findByUser(user);

        Cart cart = null;

        try{
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e)
        {
            cart = new Cart();
            cart.setCart_id(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());


        }

        //perform cart operations
        //if cart items already present, then update
        AtomicReference<Boolean> updated = new AtomicReference<>();
        List<CartItem> items = cart.getItems();
        List<CartItem> updatedItems = items.stream().map(item->{
            if(item.getProduct().getProductId().equals(productId))
            {
                //item already present
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getPrice());
                updated.set(true);

            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);


        //create items
        if(!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem);
        }

        cart.setUser(user);

        Cart updatedCart = cartRepository.save(cart);

        return mapper.map(cart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {

    }

    @Override
    public void clearCart(String userId) {

    }
}
