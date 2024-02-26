package com.example.filemanagedemo.repository;

import com.example.filemanagedemo.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT new Document(d.id, d.name, d.size) FROM Document d ORDER BY d.uploadTime DESC")
    List<Document> findAllBy();

    @Query("SELECT d FROM Document d " +
            "JOIN FETCH d.project p " +
            "JOIN FETCH p.projectAccesses pa " +
            "JOIN FETCH pa.user u " +
            "JOIN FETCH pa.role r " +
            "WHERE u.userId = :userId " +
            "AND r.roleName = :roleName " +
            "AND p.projectId = :projectId")
    List<Document> findDocumentsForUserByRoleAndProject(@Param("userId") Integer userId, @Param("roleName") String roleName, @Param("projectId") Integer projectId);
}
