package com.example.userauthenticationservice.repos;

import com.example.userauthenticationservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,Long> {

    Role save(Role role);

    Optional<Role> findByValue(String value);

}
