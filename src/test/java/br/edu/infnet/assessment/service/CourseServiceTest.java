package br.edu.infnet.assessment.service;

import br.edu.infnet.assessment.config.exceptions.NotFoundException;
import br.edu.infnet.assessment.dto.CourseRequest;
import br.edu.infnet.assessment.entities.Course;
import br.edu.infnet.assessment.entities.Enrollment;
import br.edu.infnet.assessment.entities.Student;
import br.edu.infnet.assessment.repository.CourseRepository;
import br.edu.infnet.assessment.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

	CourseRepository courseRepository;
	EnrollmentRepository enrollmentRepository;
	CourseService service;

	@BeforeEach
	void setUp() {
		courseRepository = mock(CourseRepository.class);
		enrollmentRepository = mock(EnrollmentRepository.class);
		service = new CourseService(courseRepository, enrollmentRepository);
	}

	@Test
	void listAll_returnsRepositoryResult() {
		when(courseRepository.findAll()).thenReturn(List.of(new Course()));
		var all = service.listAll();
		assertEquals(1, all.size());
		verify(courseRepository).findAll();
	}

	@Test
	void create_throwsWhenCodeAlreadyExists() {
		when(courseRepository.findByCode("MATH1")).thenReturn(Optional.of(new Course()));
		var req = new CourseRequest("Mathematics", "MATH1");
		assertThrows(IllegalArgumentException.class, () -> service.create(req));
		verify(courseRepository, never()).save(any());
	}

	@Test
	void create_persistsAndReturnsResponse() {
		when(courseRepository.findByCode("MATH1")).thenReturn(Optional.empty());
		when(courseRepository.save(any(Course.class))).thenAnswer(inv -> {
			Course c = inv.getArgument(0);
			c.setId(10L);
			return c;
		});

		var resp = service.create(new CourseRequest("Mathematics", "MATH1"));
		assertEquals(10L, resp.id());
		assertEquals("Mathematics", resp.name());
		assertEquals("MATH1", resp.code());
		verify(courseRepository).save(any(Course.class));
	}

	@Test
	void updateCourse_updatesNameAndKeepsCodeFromPath() {
		Course existing = new Course();
		existing.setId(5L);
		existing.setCode("MATH1");
		existing.setName("Old");
		when(courseRepository.findByCode("MATH1")).thenReturn(Optional.of(existing));
		when(courseRepository.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

		var resp = service.updateCourse("MATH1", new CourseRequest("New Name", "IGNORED"));
		assertEquals(5L, resp.id());
		assertEquals("New Name", resp.name());
		assertEquals("MATH1", resp.code()); // service mantém o code do path
	}

	@Test
	void updateCourse_throwsWhenNotFound() {
		when(courseRepository.findByCode("X")).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class,
				() -> service.updateCourse("X", new CourseRequest("N", "C")));
	}

	@Test
	void deleteCourse_removesWhenExists() {
		Course existing = new Course();
		when(courseRepository.findByCode("M")).thenReturn(Optional.of(existing));

		service.deleteCourse("M");
		verify(courseRepository).delete(existing);
	}

	@Test
	void deleteCourse_throwsWhenNotFound() {
		when(courseRepository.findByCode("NOPE")).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> service.deleteCourse("NOPE"));
	}

	@Test
	void approvedStudents_returnsNamesFromEnrollments() {
		Course c = new Course();
		c.setId(1L);
		when(courseRepository.findByCode("M")).thenReturn(Optional.of(c));

		Enrollment e1 = new Enrollment();
		Student s1 = new Student();
		s1.setName("Alice");
		e1.setStudent(s1);

		Enrollment e2 = new Enrollment();
		Student s2 = new Student();
		s2.setName("Bob");
		e2.setStudent(s2);

		when(enrollmentRepository.findByCourseIdAndGradeGreaterThanEqual(1L, 7.0))
				.thenReturn(List.of(e1, e2));

		var names = service.approvedStudents("M");
		assertEquals(List.of("Alice", "Bob"), names);
	}

	@Test
	void failedStudents_returnsNamesFromEnrollments() {
		Course c = new Course();
		c.setId(1L);
		when(courseRepository.findByCode("M")).thenReturn(Optional.of(c));

		Enrollment e = new Enrollment();
		Student s = new Student();
		s.setName("Carol");
		e.setStudent(s);

		when(enrollmentRepository.findByCourseIdAndGradeLessThan(1L, 7.0))
				.thenReturn(List.of(e));

		var names = service.failedStudents("M");
		assertEquals(List.of("Carol"), names);
	}
}
