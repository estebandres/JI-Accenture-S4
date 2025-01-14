package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.AppUserDTO;
import com.mindhub.todolist.dtos.RegisterUserDTO;
import com.mindhub.todolist.dtos.UpdateAppUserDto;
import com.mindhub.todolist.exceptions.UserAlreadyExistsException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface AppUserService {
    List<AppUserDTO> getAllUsers();
    AppUserDTO getUserById(Long id) throws UserNotFoundException;
    AppUserDTO createUser(AppUserDTO userDTO);
    AppUserDTO updateUser(Long id, AppUserDTO userDTO);
    void deleteUser(Long id);

    AppUserDTO registerNewUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistsException;

    AppUserDTO getLoggedInUserInfo(String email) throws UserNotFoundException;

    AppUserDTO updateLoggedInUserInfo(String email, UpdateAppUserDto updateAppUserDto) throws UserNotFoundException;

    void deleteLoggedInUser(String email) throws UserNotFoundException;

    AppUserDTO replaceLoggedInUserInfo(String email, UpdateAppUserDto updateAppUserDto) throws UserNotFoundException, BadRequestException;
}
