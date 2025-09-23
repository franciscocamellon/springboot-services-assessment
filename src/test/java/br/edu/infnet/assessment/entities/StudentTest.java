package br.edu.infnet.assessment.entities;

import br.edu.infnet.assessment.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentTest {

	@Autowired
	StudentRepository studentRepository;

	@Test
	void prePersist_shouldSetCreatedAt() {
		Student s = Student.builder()
				.name("Alice")
				.cpf("11122233344")
				.email("alice@example.com")
				.phone("+55 31 99999-0000")
				.address("Street A")
				.build();

		Student saved = studentRepository.saveAndFlush(s);
		assertNotNull(saved.getId());
		assertNotNull(saved.getCreatedAt());
		assertNull(saved.getUpdatedAt());
	}

	@Test
	void preUpdate_shouldSetUpdatedAt_andKeepCreatedAt() {
		Student s = Student.builder()
				.name("Bob")
				.cpf("55566677788")
				.email("bob@example.com")
				.phone("999")
				.address("Addr")
				.build();

		Student saved = studentRepository.saveAndFlush(s);
		OffsetDateTime created = saved.getCreatedAt();

		saved.setName("Bob Prime");
		Student updated = studentRepository.saveAndFlush(saved);

		assertNotNull(updated.getUpdatedAt());
		assertEquals(created, updated.getCreatedAt());
		assertTrue(updated.getUpdatedAt().isAfter(updated.getCreatedAt()) || updated.getUpdatedAt().isEqual(updated.getCreatedAt()));
	}

	@Test
	void uniqueCpf_shouldEnforceConstraint() {
		Student a = Student.builder()
				.name("Carol")
				.cpf("00011122233")
				.email("c@example.com").phone("1").address("x").build();
		studentRepository.saveAndFlush(a);

		Student b = Student.builder()
				.name("Carol 2")
				.cpf("00011122233") // duplicate
				.email("c2@example.com").phone("2").address("y").build();

		assertThrows(DataIntegrityViolationException.class, () -> {
			studentRepository.saveAndFlush(b);
		});
	}
}
