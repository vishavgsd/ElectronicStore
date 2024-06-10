package com.mycompany.electronicstore.controllers;

import com.mycompany.electronicstore.dtos.ApiResponseMessage;
import com.mycompany.electronicstore.dtos.UserDto;
import com.mycompany.electronicstore.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
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

}
