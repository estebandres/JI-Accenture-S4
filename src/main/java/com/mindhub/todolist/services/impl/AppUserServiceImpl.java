package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.AppUserDTO;
import com.mindhub.todolist.dtos.RegisterUserDTO;
import com.mindhub.todolist.dtos.UpdateAppUserDto;
import com.mindhub.todolist.exceptions.UserAlreadyExistsException;
import com.mindhub.todolist.models.AppUser;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.repositories.AppUserRepository;
import com.mindhub.todolist.services.AppUserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.stream.Collectors;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserServiceImpl(PasswordEncoder passwordEncoder, AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public AppUserDTO registerNewUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistsException {
        if(appUserRepository.existsAppUserByEmail(registerUserDTO.email())){
            throw new UserAlreadyExistsException(registerUserDTO.email());
        }
        AppUser user = new AppUser();
        user.setEmail(registerUserDTO.email());
        user.setUsername(registerUserDTO.username());
        user.setPassword(registerUserDTO.password());
        AppUser savedUser = appUserRepository.save(user);

        return new AppUserDTO(savedUser);
    }

    @Override
    public AppUserDTO getLoggedInUserInfo(String email) throws UserNotFoundException {
        AppUser user = this.appUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return new AppUserDTO(user);
    }

    @Override
    public AppUserDTO updateLoggedInUserInfo(String email, UpdateAppUserDto updateAppUserDto) throws UserNotFoundException {
        AppUser user = this.appUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        if (updateAppUserDto.username() != null) {
            user.setUsername(updateAppUserDto.username());
        }
        if (updateAppUserDto.email() != null) {
            user.setEmail(updateAppUserDto.email());
        }
        if (updateAppUserDto.password() != null) {
            user.setPassword(passwordEncoder.encode(updateAppUserDto.password()));
        }
        AppUser savedUser = appUserRepository.save(user);
        return new AppUserDTO(savedUser);
    }

    @Override
    public void deleteLoggedInUser(String email) throws UserNotFoundException {
        AppUser user = this.appUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        appUserRepository.delete(user);
    }

    @Override
    public AppUserDTO replaceLoggedInUserInfo(String email, UpdateAppUserDto updateAppUserDto) throws UserNotFoundException, BadRequestException {
        AppUser user = this.appUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        if (updateAppUserDto.username() == null) {
            throw new BadRequestException("Missing username attribute");
        }
        if (updateAppUserDto.email() == null) {
            throw new BadRequestException("Missing email attribute");
        }
        if (updateAppUserDto.password() == null) {
            throw new BadRequestException("Missing password attribute");
        }
        user.setUsername(updateAppUserDto.username());
        user.setEmail(updateAppUserDto.email());
        user.setPassword(passwordEncoder.encode(updateAppUserDto.password()));
        AppUser savedUser = appUserRepository.save(user);
        return new AppUserDTO(savedUser);
    }
}
