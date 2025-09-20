package br.edu.infnet.assessment.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;

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

	@Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof Student)) return false; Student s=(Student)o; return Objects.equals(id,s.id);}
	@Override public int hashCode(){return Objects.hash(id);}
}
