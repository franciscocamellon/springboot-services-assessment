package br.edu.infnet.assessment.model;

import br.edu.infnet.assessment.repository.CourseRepository;
import br.edu.infnet.assessment.repository.EnrollmentRepository;
import br.edu.infnet.assessment.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EnrollmentTest {

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	CourseRepository courseRepository;

	private Student student(String cpf) {
		return studentRepository.saveAndFlush(
				Student.builder()
						.name("Student " + cpf)
						.cpf(cpf)
						.email("s"+cpf+"@example.com")
						.phone("999")
						.address("Addr")
						.build()
		);
	}

	private Course course(String code) {
		Course course = new Course();
		course.setName("Course " + code);
		course.setCode(code);
		return courseRepository.saveAndFlush(course);
	}

	@Test
	void prePersist_shouldSetCreatedAt_andAllowNullGrade() {
		Student student = student("12345678901");
		Course course = course("C1");

		Enrollment enrollment = new Enrollment();
		enrollment.setStudent(student);
		enrollment.setCourse(course);

		Enrollment saved = enrollmentRepository.saveAndFlush(enrollment);
		assertNotNull(saved.getId());
		assertNull(saved.getGrade());
		assertNotNull(saved.getCreatedAt());
		assertNull(saved.getUpdatedAt());
	}

	@Test
	void preUpdate_shouldSetUpdatedAt() {
		Student student = student("12345678901");
		Course course = course("C1");

		Enrollment enrollment = new Enrollment();
		enrollment.setStudent(student);
		enrollment.setCourse(course);
		enrollment = enrollmentRepository.saveAndFlush(enrollment);

		enrollment.setGrade(9.0);
		Enrollment updated = enrollmentRepository.saveAndFlush(enrollment);

		assertNotNull(updated.getUpdatedAt());
		assertTrue(updated.getUpdatedAt().isAfter(updated.getCreatedAt()) || updated.getUpdatedAt().isEqual(updated.getCreatedAt()));
		assertEquals(9.0, updated.getGrade());
	}

	@Test
	void nullStudentOrCourse_shouldViolateNotNullForeignKey() {
		Course course = course("C3");

		Enrollment withoutStudent = new Enrollment();
		withoutStudent.setCourse(course);

		Student student = student("55544433322");
		Enrollment withoutCourse = new Enrollment();
		withoutCourse.setStudent(student);

		assertThrows(DataIntegrityViolationException.class, () -> enrollmentRepository.saveAndFlush(withoutStudent));

		assertThrows(DataIntegrityViolationException.class, () -> enrollmentRepository.saveAndFlush(withoutCourse));
	}
}