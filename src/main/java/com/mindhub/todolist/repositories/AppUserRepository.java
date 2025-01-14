package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    public Optional<AppUser> findByEmail(String email);

    boolean existsAppUserByEmail(String email);
}
