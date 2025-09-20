package br.edu.infnet.assessment.service;

import br.edu.infnet.assessment.dto.StudentRequest;
import br.edu.infnet.assessment.dto.StudentResponse;
import br.edu.infnet.assessment.model.Student;
import br.edu.infnet.assessment.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentService {

	private final StudentRepository repository;

	@Transactional
	public StudentResponse create(StudentRequest studentRequest){

		repository.findByCpf(studentRequest.cpf())
				.ifPresent(s -> { throw new IllegalArgumentException("CPF already registered"); });

		Student newStudent = new Student();
		newStudent.setName(studentRequest.name());
		newStudent.setCpf(studentRequest.cpf());
		newStudent.setEmail(studentRequest.email());
		newStudent.setPhone(studentRequest.phone());
		newStudent.setAddress(studentRequest.address());

		return toResponse(repository.save(newStudent));
	}

	@Transactional(readOnly=true)
	public List<StudentResponse> listAll(){
		return repository.findAll().stream().map(this::toResponse).toList();
	}

	public StudentResponse toResponse(Student student){
		return new StudentResponse(student.getId(), student.getName(), student.getCpf(), student.getEmail(), student.getPhone(), student.getAddress());
	}
}
