package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);


    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {


        int quantity = request.getQuantity();
        String  productId = request.getProductId();

        if(quantity<=0)
            throw   new BadApiRequestException("Requested quantity is not valid !!");

        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product with this id not found!!"));

        //fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found !!"));



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
        updated.set(false);
        List<CartItem> items = cart.getItems();
        List<CartItem> updatedItems = items.stream().map(item->{
            if(item.getProduct().getProductId().equals(productId))
            {
                //item already present
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
                updated.set(true);

            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);


        //create items
        if(!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem);
        }

        cart.setUser(user);

        Cart updatedCart = cartRepository.save(cart);

        logger.info(String.valueOf(updatedCart));

        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {

       CartItem cartItem1 =  cartItemRepository.findById(cartItemId).orElseThrow(()-> new ResourceNotFoundException("cart item not found in db"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {

        //fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found in database"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("cart not found for this user"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found in database"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("cart not found for this user"));
        return mapper.map(cart, CartDto.class);
    }
}
