package br.edu.infnet.assessment.service;

import br.edu.infnet.assessment.config.NotFoundException;
import br.edu.infnet.assessment.dto.CourseRequest;
import br.edu.infnet.assessment.dto.CourseResponse;
import br.edu.infnet.assessment.model.Course;
import br.edu.infnet.assessment.repository.CourseRepository;
import br.edu.infnet.assessment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CourseService {

	private final CourseRepository courseRepository;
	private final EnrollmentRepository enrollmentRepository;

	@Transactional(readOnly=true)
	public List<Course> listAll(){
		return courseRepository.findAll();
	}

	@Transactional
	public CourseResponse create(CourseRequest courseRequest) {
		courseRepository.findByCode(courseRequest.code())
				.ifPresent(c -> { throw new IllegalArgumentException("Código de curso já existente!"); });

		Course newCourse = new Course();
		newCourse.setCode(courseRequest.code().toLowerCase());
		newCourse.setName(courseRequest.name());

		return toResponse(courseRepository.save(newCourse));
	}

	@Transactional
	public CourseResponse updateCourse(String code, CourseRequest courseRequest) {
		Course existingCourse = courseRepository.findByCode(code)
				.orElseThrow(() -> new NotFoundException("Curso %s não encontrado!".formatted(code)));

		if (existingCourse != null) {
			existingCourse.setName(courseRequest.name());
			existingCourse.setCode(code.toLowerCase());
			return toResponse(courseRepository.save(existingCourse));
		}

		return null;
	}

	@Transactional
	public void deleteCourse(String code) {
		Course existingCourse = courseRepository.findByCode(code)
				.orElseThrow(() -> new NotFoundException("Curso %s não encontrado!".formatted(code)));
		courseRepository.delete(existingCourse);
	}

	@Transactional(readOnly=true)
	public List<String> approvedStudents(String code){
		Course existingCourse = courseRepository.findByCode(code)
				.orElseThrow(() -> new NotFoundException("Curso %s não encontrado!".formatted(code)));

		return enrollmentRepository.findByCourseIdAndGradeGreaterThanEqual(existingCourse.getId(), 7.0)
				.stream().map(e->e.getStudent().getName()).toList();
	}

	@Transactional(readOnly=true)
	public List<String> failedStudents(String code){
		Course existingCourse = courseRepository.findByCode(code)
				.orElseThrow(() -> new NotFoundException("Curso %s não encontrado!".formatted(code)));

		return enrollmentRepository.findByCourseIdAndGradeLessThan(existingCourse.getId(), 7.0).stream().map(e->e.getStudent().getName()).toList();
	}

	public CourseResponse toResponse(Course course){
		return new CourseResponse(course.getId(), course.getName(), course.getCode(), course.getCreatedAt(), course.getUpdatedAt());
	}

}
