package com.mycompany.electronicstore.services;

import com.mycompany.electronicstore.dtos.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto updateUser(String userId, UserDto userDto);
    void deleteUser(String userId);
    UserDto getUserById(String userId);
    UserDto getUserByEmail(String email);
    List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
    List<UserDto> searchUser(String keyword);
}
