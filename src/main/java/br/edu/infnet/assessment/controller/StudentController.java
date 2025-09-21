package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.StudentRequest;
import br.edu.infnet.assessment.dto.StudentResponse;
import br.edu.infnet.assessment.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;

	@GetMapping
	public List<StudentResponse> list(){
		return studentService.listAll();
	}

	@PostMapping
	public ResponseEntity<StudentResponse> create(@RequestBody @Valid StudentRequest studentRequest){
		return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(studentRequest));
	}

	@PatchMapping("/{cpf}")
	public ResponseEntity<StudentResponse> update(@PathVariable(name = "cpf") String cpf, @RequestBody @Valid StudentRequest studentRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(studentService.update(cpf, studentRequest));
	}

	@DeleteMapping("/{cpf}")
	public ResponseEntity<StudentResponse> delete(@PathVariable(name = "cpf") String cpf) {
		studentService.delete(cpf);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
