package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.AppUserDTO;
import com.mindhub.todolist.entities.AppUser;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.repositories.AppUserRepository;
import com.mindhub.todolist.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<AppUserDTO> getAllUsers() {
        return appUserRepository.findAll().stream()
                .map(AppUserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public AppUserDTO getUserById(Long id) throws UserNotFoundException {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return new AppUserDTO(user);
    }

    @Override
    public AppUserDTO createUser(AppUserDTO userDTO) {
        AppUser user = new AppUser();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword("defaultPassword"); // Set a default or hashed password
        AppUser savedUser = appUserRepository.save(user);
        return new AppUserDTO(savedUser);
    }

    @Override
    public AppUserDTO updateUser(Long id, AppUserDTO userDTO) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        AppUser updatedUser = appUserRepository.save(user);
        return new AppUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        appUserRepository.deleteById(id);
    }
}
