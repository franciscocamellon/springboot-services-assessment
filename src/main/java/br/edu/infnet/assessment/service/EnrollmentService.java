package br.edu.infnet.assessment.service;

import br.edu.infnet.assessment.dto.EnrollmentRequest;
import br.edu.infnet.assessment.dto.GradeRequest;
import br.edu.infnet.assessment.model.Course;
import br.edu.infnet.assessment.model.Enrollment;
import br.edu.infnet.assessment.model.Student;
import br.edu.infnet.assessment.repository.CourseRepository;
import br.edu.infnet.assessment.repository.EnrollmentRepository;
import br.edu.infnet.assessment.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EnrollmentService {

	private final EnrollmentRepository enrollmentRepository;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;

	public List<Enrollment> listAll() {
		return enrollmentRepository.findAll();
	}

	@Transactional
	public Long enroll(EnrollmentRequest enrollmentRequest){
		Student student = studentRepository.findById(enrollmentRequest.studentId())
				.orElseThrow(() -> new IllegalArgumentException("Student not found"));
		Course course = courseRepository.findById(enrollmentRequest.courseId())
				.orElseThrow(() -> new IllegalArgumentException("Course not found"));

		Enrollment newEnrollment = new Enrollment();
		newEnrollment.setStudent(student);
		newEnrollment.setCourse(course);

		return enrollmentRepository.save(newEnrollment).getId();
	}

	@Transactional
	public void assignGrade(Long enrollmentId, GradeRequest gradeRequest){
		Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
				.orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
		enrollment.setGrade(gradeRequest.grade());
		enrollmentRepository.save(enrollment);
	}

	public void withdrawal(Long id) {
		Enrollment enrollment = enrollmentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
		enrollmentRepository.delete(enrollment);
	}

}
