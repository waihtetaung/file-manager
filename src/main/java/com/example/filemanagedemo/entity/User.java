package com.example.filemanagedemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String name;
    private String email;
    private String password;
    @OneToOne
    private Role role;
    @ManyToMany
    private List<Project> projects = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<ProjectAccess> projectAccesses = new ArrayList<>();
}
