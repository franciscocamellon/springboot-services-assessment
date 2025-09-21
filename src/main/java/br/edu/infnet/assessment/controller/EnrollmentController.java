package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.EnrollmentRequest;
import br.edu.infnet.assessment.dto.GradeRequest;
import br.edu.infnet.assessment.model.Enrollment;
import br.edu.infnet.assessment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

	private final EnrollmentService enrollmentService;

	@GetMapping
	public List<Enrollment> list() {
		return enrollmentService.listAll();
	}

	@PostMapping
	public Long enroll(@RequestBody @Valid EnrollmentRequest enrollmentRequest){
		return enrollmentService.enroll(enrollmentRequest);
	}

	@PutMapping("/{id}/grade")
	public void assignGrade(@PathVariable Long id, @RequestBody @Valid GradeRequest gradeRequest){
		enrollmentService.assignGrade(id, gradeRequest);
	}

	@DeleteMapping("/{id}")
	public void withdrawal(@PathVariable Long id){
		enrollmentService.withdrawal(id);
	}
}
