package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.AppUserDTO;
import com.mindhub.todolist.dtos.RegisterUserDTO;
import com.mindhub.todolist.exceptions.UserAlreadyExistsException;
import com.mindhub.todolist.exceptions.UserNotFoundException;

import java.util.List;

public interface AppUserService {
    List<AppUserDTO> getAllUsers();
    AppUserDTO getUserById(Long id) throws UserNotFoundException;
    AppUserDTO createUser(AppUserDTO userDTO);
    AppUserDTO updateUser(Long id, AppUserDTO userDTO);
    void deleteUser(Long id);

    AppUserDTO registerNewUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistsException;
}
