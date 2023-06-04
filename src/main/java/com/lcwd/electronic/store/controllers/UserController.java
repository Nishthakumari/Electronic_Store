package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class UserController {

    @Autowired
    private UserService userService;

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody  UserDto userDto){
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }



    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(

            @PathVariable ("userId") String userId,
            @RequestBody UserDto userDto){

        UserDto updatedUserDto = userService.updateUser(userDto, userId);
        return new  ResponseEntity<>(updatedUserDto, HttpStatus.OK);

    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable ("userId") String userId)
    {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage
                .builder()
                .message("User is deleted successfully !!!")
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }



    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId)
    {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    //get by email
    @GetMapping("/email/{userId}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String userId)
    {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    //search user
    public ResponseEntity<List<UserDto>> searchUser(String keywords)
    {
        return  new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
    }
}
