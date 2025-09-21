package br.edu.infnet.assessment.service;

import br.edu.infnet.assessment.config.NotFoundException;
import br.edu.infnet.assessment.dto.StudentRequest;
import br.edu.infnet.assessment.dto.StudentResponse;
import br.edu.infnet.assessment.model.Student;
import br.edu.infnet.assessment.repository.StudentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentService {

	private final StudentRepository studentRepository;

	@Transactional(readOnly=true)
	public List<StudentResponse> listAll(){
		return studentRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional
	public StudentResponse create(StudentRequest studentRequest){

		studentRepository.findByCpf(studentRequest.cpf())
				.ifPresent(s -> { throw new IllegalArgumentException("CPF já cadastrado para outro aluno!"); });

		Student newStudent = new Student();
		newStudent.setName(studentRequest.name());
		newStudent.setCpf(studentRequest.cpf());
		newStudent.setEmail(studentRequest.email());
		newStudent.setPhone(studentRequest.phone());
		newStudent.setAddress(studentRequest.address());

		return toResponse(studentRepository.save(newStudent));
	}

	public StudentResponse update(String cpf, @Valid StudentRequest studentRequest) {

		Student exisitingStudent = studentRepository.findByCpf(studentRequest.cpf())
				.orElseThrow(() -> new NotFoundException("Estudante %s não encontrado!".formatted(cpf)));

		if (exisitingStudent != null) {
			exisitingStudent.setName(studentRequest.name());
			exisitingStudent.setCpf(studentRequest.cpf());
			exisitingStudent.setEmail(studentRequest.email());
			exisitingStudent.setPhone(studentRequest.phone());
			exisitingStudent.setAddress(studentRequest.address());

			return toResponse(studentRepository.save(exisitingStudent));
		}

		return null;
	}

	public void delete(String cpf) {
		Student exisitingStudent = studentRepository.findByCpf(cpf)
				.orElseThrow(() -> new NotFoundException("Estudante %s não encontrado!".formatted(cpf)));
		studentRepository.delete(exisitingStudent);
	}

	public StudentResponse toResponse(Student student){
		return new StudentResponse(student.getId(), student.getName(), student.getCpf(), student.getEmail(), student.getPhone(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt());
	}
}
