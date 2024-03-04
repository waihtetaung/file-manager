package com.example.filemanagedemo.repository;

import com.example.filemanagedemo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
