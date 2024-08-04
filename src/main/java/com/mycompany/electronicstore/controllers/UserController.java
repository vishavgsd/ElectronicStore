package com.mycompany.electronicstore.controllers;

import com.mycompany.electronicstore.dtos.ApiResponseMessage;
import com.mycompany.electronicstore.dtos.ImageResponse;
import com.mycompany.electronicstore.dtos.PageableResponse;
import com.mycompany.electronicstore.dtos.UserDto;
import com.mycompany.electronicstore.services.FileService;
import com.mycompany.electronicstore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUserDto = userService.updateUser(userId, userDto);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User deleted")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false)  int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false)  String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false)  String sortDir
    ) {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keyword") String keyword) {
        return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.OK);
    }


    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @PathVariable("userId") String userId,
            @RequestParam("userImage")MultipartFile image
    ) throws IOException {

        String imageName = fileService.uploadFile(image, imageUploadPath);
        UserDto userDto = userService.getUserById(userId);
        userDto.setImageName(imageName);
        UserDto updatedDto = userService.updateUser(userId, userDto);
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }

    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {

        UserDto userDto = userService.getUserById(userId);
        logger.info("User Image Name : {} ", userDto.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, userDto.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
