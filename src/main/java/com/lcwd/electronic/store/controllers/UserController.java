package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody  @Valid UserDto userDto){
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }



    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(

            @PathVariable ("userId") String userId,
            @RequestBody @Valid  UserDto userDto) {

        UserDto updatedUserDto = userService.updateUser(userDto, userId);
        return new  ResponseEntity<>(updatedUserDto, HttpStatus.OK);

    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable ("userId") String userId) throws Throwable {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage
                .builder()
                .message("User is deleted successfully !!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
             @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String  sortDir
    ){
        return new ResponseEntity<>(userService.getAllUser(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }



    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    //get by email
    @GetMapping("/email/{userEmail}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String userEmail) {
        return new ResponseEntity<>(userService.getUserByEmail(userEmail), HttpStatus.OK);
    }

    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable  String keywords)
    {
        return  new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
    }


    //upload user image
    @PostMapping("/image/{userId}")
    public  ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage")MultipartFile image,
            @PathVariable String userId
            ) throws IOException {

        String  imageName = fileService.uploadFile(image, imageUploadPath);

        UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = userService.updateUser(user, userId);

        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

    }

    //serve user image

    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String  userId, HttpServletResponse response) throws IOException {

        UserDto user = userService.getUserById(userId);
        InputStream resource  = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());


    }

}
