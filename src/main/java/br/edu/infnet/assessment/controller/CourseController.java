package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.CourseRequest;
import br.edu.infnet.assessment.dto.CourseResponse;
import br.edu.infnet.assessment.model.Course;
import br.edu.infnet.assessment.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

	private final CourseService courseService;

	@GetMapping
	public ResponseEntity<Collection<Course>> list(){
		return ResponseEntity.ok(courseService.listAll());
	}

	@PostMapping
	public ResponseEntity<CourseResponse> create(@RequestBody CourseRequest courseRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(courseRequest));
	}

	@GetMapping("/{id}/approved")
	public List<String> approved(@PathVariable Long id){
		return courseService.approvedStudents(id);
	}

	@GetMapping("/{id}/failed")
	public List<String> failed(@PathVariable Long id){
		return courseService.failedStudents(id);
	}

}
