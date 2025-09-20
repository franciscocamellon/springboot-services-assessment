package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.EnrollmentRequest;
import br.edu.infnet.assessment.dto.GradeRequest;
import br.edu.infnet.assessment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

	private final EnrollmentService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long enroll(@RequestBody @Valid EnrollmentRequest req){
		return service.enroll(req);
	}

	@PutMapping("/{id}/grade")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void assignGrade(@PathVariable Long id, @RequestBody @Valid GradeRequest req){
		service.assignGrade(id, req);
	}
}
