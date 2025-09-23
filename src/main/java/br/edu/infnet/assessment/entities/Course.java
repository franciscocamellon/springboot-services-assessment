package br.edu.infnet.assessment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(unique=true, nullable=false, length=20)
    private String code;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    @PrePersist
    void prePersist() { this.createdAt = OffsetDateTime.now(); }

    @PreUpdate
    void preUpdate() { this.updatedAt = OffsetDateTime.now(); }
}
