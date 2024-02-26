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
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;
    private String projectName;

    @OneToMany(mappedBy = "project")
    private List<Document> documents = new ArrayList<>();

    @ManyToMany(mappedBy = "projects")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<ProjectAccess> projectAccesses = new ArrayList<>();
}


