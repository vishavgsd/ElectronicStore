package com.mycompany.electronicstore.services.impl;

import com.mycompany.electronicstore.dtos.UserDto;
import com.mycompany.electronicstore.entities.User;
import com.mycompany.electronicstore.repositories.UserRepository;
import com.mycompany.electronicstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDto createUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        UserDto newDto = entityToDto(savedUser);
        return newDto;
    }

    private UserDto entityToDto(User user) {
        UserDto userDto = UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .about(user.getAbout())
                .password(user.getPassword())
                .imageName(user.getImageName())
                .build();
        return userDto;
    }

    private User dtoToEntity(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .about(userDto.getAbout())
                .password(userDto.getPassword())
                .imageName(userDto.getImageName())
                .build();
        return user;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User savedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(savedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with given email"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }
}
