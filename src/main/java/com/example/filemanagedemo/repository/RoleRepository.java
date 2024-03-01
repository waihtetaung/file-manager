package com.example.filemanagedemo.repository;

import com.example.filemanagedemo.entity.Role;
import com.example.filemanagedemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByUser(User user);
}
