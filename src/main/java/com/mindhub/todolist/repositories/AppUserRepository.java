package com.mindhub.todolist.repositories;

import com.mindhub.todolist.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

}
