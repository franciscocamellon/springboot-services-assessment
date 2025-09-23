package br.edu.infnet.assessment.entities;

import br.edu.infnet.assessment.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseTest {

	@Autowired
	CourseRepository courseRepository;

	@Test
	void prePersist_shouldSetCreatedAt() {
		Course c = new Course();
		c.setName("Mathematics");
		c.setCode("MATH1");

		Course saved = courseRepository.saveAndFlush(c);
		assertNotNull(saved.getId());
		assertNotNull(saved.getCreatedAt());
		assertNull(saved.getUpdatedAt());
	}

	@Test
	void preUpdate_shouldSetUpdatedAt_andKeepCreatedAt() throws InterruptedException {
		Course c = new Course();
		c.setName("Mathematics");
		c.setCode("MATH2");
		Course saved = courseRepository.saveAndFlush(c);

		OffsetDateTime created = saved.getCreatedAt();
		assertNotNull(created);

		// mutate + update
		saved.setName("Advanced Mathematics");
		Course updated = courseRepository.saveAndFlush(saved);

		assertNotNull(updated.getUpdatedAt());
		assertEquals(created, updated.getCreatedAt());
		assertTrue(updated.getUpdatedAt().isAfter(updated.getCreatedAt()) || updated.getUpdatedAt().isEqual(updated.getCreatedAt()));
	}

	@Test
	void uniqueCode_shouldEnforceConstraint() {
		Course a = new Course();
		a.setName("Physics");
		a.setCode("PHY1");
		courseRepository.saveAndFlush(a);

		Course b = new Course();
		b.setName("New Physics");
		b.setCode("PHY1"); // duplicate

		assertThrows(DataIntegrityViolationException.class, () -> {
			courseRepository.saveAndFlush(b);
		});
	}
}
