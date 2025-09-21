package br.edu.infnet.assessment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private Student student;

    @ManyToOne(optional=false)
    private Course course;

    private Double grade;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

	@PrePersist
    void prePersist() { this.createdAt = OffsetDateTime.now(); }

    @PreUpdate
    void preUpdate() { this.updatedAt = OffsetDateTime.now(); }

}
