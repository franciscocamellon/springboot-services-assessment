package br.edu.infnet.assessment.service;

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

	private final CourseRepository repository;
	private final EnrollmentRepository enrollmentRepository;

	@Transactional(readOnly=true)
	public List<Course> listAll(){
		return repository.findAll();
	}



	@Transactional(readOnly=true)
	public List<String> approvedStudents(Long courseId){
		return enrollmentRepository.findByCourseIdAndGradeGreaterThanEqual(courseId, 7.0)
				.stream().map(e->e.getStudent().getName()).toList();
	}

	@Transactional(readOnly=true)
	public List<String> failedStudents(Long courseId){
		return enrollmentRepository.findByCourseIdAndGradeLessThan(courseId, 7.0).stream().map(e->e.getStudent().getName()).toList();
	}

	public CourseResponse toResponse(Course c){
		return new CourseResponse(c.getId(), c.getName(), c.getCode());
	}

	@Transactional
	public CourseResponse create(CourseRequest courseRequest) {
		repository.findByCode(courseRequest.code())
				.ifPresent(c -> { throw new IllegalArgumentException("Course code already registered"); });

		Course newCourse = new Course();
		newCourse.setCode(courseRequest.code());
		newCourse.setName(courseRequest.name());

		return toResponse(repository.save(newCourse));
	}
}
