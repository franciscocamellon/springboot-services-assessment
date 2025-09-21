package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.CourseRequest;
import br.edu.infnet.assessment.dto.CourseResponse;
import br.edu.infnet.assessment.model.Course;
import br.edu.infnet.assessment.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
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

	@PutMapping("/{code}")
	public ResponseEntity<CourseResponse> update(@PathVariable(name = "code") String code, @RequestBody CourseRequest courseRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.updateCourse(code, courseRequest));
	}

	@DeleteMapping("/{code}")
	public ResponseEntity<CourseResponse> delete(@PathVariable(name = "code") String code) {
		courseService.deleteCourse(code);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{code}/approved")
	public ResponseEntity<List<String>> approved(@PathVariable(name = "code") String code){
		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.approvedStudents(code));
	}

	@GetMapping("/{code}/failed")
	public ResponseEntity<List<String>> failed(@PathVariable(name = "code") String code){
		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.failedStudents(code));
	}

}
