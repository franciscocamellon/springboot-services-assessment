package br.edu.infnet.assessment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity @Table(name = "students")
public class Student {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true, nullable=false, length=14)
	private String cpf;

	@Email
	private String email;

	private String phone;

	private String address;

	@Column(nullable = false)
	private OffsetDateTime createdAt;

	private OffsetDateTime updatedAt;

	@PrePersist
	void prePersist() { this.createdAt = OffsetDateTime.now(); }

	@PreUpdate
	void preUpdate() { this.updatedAt = OffsetDateTime.now(); }

}
