package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.StudentRequest;
import br.edu.infnet.assessment.dto.StudentResponse;
import br.edu.infnet.assessment.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

	private final StudentService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public StudentResponse create(@RequestBody @Valid StudentRequest req){
		return service.create(req);
	}

	@GetMapping
	public List<StudentResponse> list(){
		return service.listAll();
	}
}
