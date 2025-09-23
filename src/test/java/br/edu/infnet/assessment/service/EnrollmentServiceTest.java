package br.edu.infnet.assessment.service;

import br.edu.infnet.assessment.dto.EnrollmentRequest;
import br.edu.infnet.assessment.dto.GradeRequest;
import br.edu.infnet.assessment.entities.Course;
import br.edu.infnet.assessment.entities.Enrollment;
import br.edu.infnet.assessment.entities.Student;
import br.edu.infnet.assessment.repository.CourseRepository;
import br.edu.infnet.assessment.repository.EnrollmentRepository;
import br.edu.infnet.assessment.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

	EnrollmentRepository enrollmentRepository;
	StudentRepository studentRepository;
	CourseRepository courseRepository;
	EnrollmentService service;

	@BeforeEach
	void setup() {
		enrollmentRepository = mock(EnrollmentRepository.class);
		studentRepository = mock(StudentRepository.class);
		courseRepository = mock(CourseRepository.class);
		service = new EnrollmentService(enrollmentRepository, studentRepository, courseRepository);
	}

	@Test
	void listAll_returnsRepositoryResult() {
		when(enrollmentRepository.findAll()).thenReturn(List.of(new Enrollment()));
		var list = service.listAll();
		assertEquals(1, list.size());
		verify(enrollmentRepository).findAll();
	}

	@Test
	void enroll_persistsWhenStudentAndCourseExist() {
		var student = new Student(); student.setId(1L);
		var course  = new Course();  course.setId(2L);

		when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
		when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
		when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> {
			Enrollment e = inv.getArgument(0);
			e.setId(99L);
			return e;
		});

		Long id = service.enroll(new EnrollmentRequest(1L, 2L));
		assertEquals(99L, id);
		verify(enrollmentRepository).save(any(Enrollment.class));
	}

	@Test
	void enroll_throwsWhenStudentNotFound() {
		when(studentRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class,
				() -> service.enroll(new EnrollmentRequest(1L, 2L)));
		verify(enrollmentRepository, never()).save(any());
	}

	@Test
	void enroll_throwsWhenCourseNotFound() {
		when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
		when(courseRepository.findById(2L)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class,
				() -> service.enroll(new EnrollmentRequest(1L, 2L)));
		verify(enrollmentRepository, never()).save(any());
	}

	@Test
	void assignGrade_updatesAndSaves() {
		Enrollment e = new Enrollment(); e.setId(50L);
		when(enrollmentRepository.findById(50L)).thenReturn(Optional.of(e));
		when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> inv.getArgument(0));

		service.assignGrade(50L, new GradeRequest(9.7));
		assertEquals(9.7, e.getGrade());
		verify(enrollmentRepository).save(e);
	}

	@Test
	void assignGrade_throwsWhenEnrollmentNotFound() {
		when(enrollmentRepository.findById(50L)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class,
				() -> service.assignGrade(50L, new GradeRequest(8.0)));
		verify(enrollmentRepository, never()).save(any());
	}

	@Test
	void withdrawal_deletesLoadedEntity() {
		Enrollment e = new Enrollment(); e.setId(77L);
		when(enrollmentRepository.findById(77L)).thenReturn(Optional.of(e));

		service.withdrawal(77L);

		verify(enrollmentRepository).delete(e);
	}

	@Test
	void withdrawal_throwsWhenEnrollmentNotFound() {
		when(enrollmentRepository.findById(77L)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> service.withdrawal(77L));
		verify(enrollmentRepository, never()).delete(any());
	}
}