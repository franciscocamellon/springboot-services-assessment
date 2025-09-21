package br.edu.infnet.assessment.service;

import br.edu.infnet.assessment.config.NotFoundException;
import br.edu.infnet.assessment.dto.StudentRequest;
import br.edu.infnet.assessment.dto.StudentResponse;
import br.edu.infnet.assessment.model.Student;
import br.edu.infnet.assessment.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

	StudentRepository repo;
	StudentService service;

	@BeforeEach
	void setUp() {
		repo = mock(StudentRepository.class);
		service = new StudentService(repo);
	}

	@Test
	void listAll_mapsToResponse() {
		Student s = new Student();
		s.setId(1L); s.setName("Alice"); s.setCpf("111"); s.setEmail("a@x");
		s.setPhone("9"); s.setAddress("Addr");
		when(repo.findAll()).thenReturn(List.of(s));

		List<StudentResponse> all = service.listAll();
		assertEquals(1, all.size());
		assertEquals("Alice", all.get(0).name());
		verify(repo).findAll();
	}

	@Test
	void create_rejectsDuplicatedCpf() {
		when(repo.findByCpf("111")).thenReturn(Optional.of(new Student()));
		var req = new StudentRequest("Alice", "111", "a@x", "9", "Addr");
		assertThrows(IllegalArgumentException.class, () -> service.create(req));
		verify(repo, never()).save(any());
	}

	@Test
	void create_savesAndReturnsResponse() {
		when(repo.findByCpf("111")).thenReturn(Optional.empty());
		when(repo.save(any(Student.class))).thenAnswer(inv -> {
			Student s = inv.getArgument(0);
			s.setId(10L);
			return s;
		});

		var resp = service.create(new StudentRequest("Alice", "111", "a@x", "9", "Addr"));
		assertEquals(10L, resp.id());
		assertEquals("Alice", resp.name());
		assertEquals("111", resp.cpf());
		verify(repo).save(any(Student.class));
	}

	@Test
	void update_throwsWhenCpfFromRequestNotFound() {
		// ATENÇÃO: o código atual busca pelo CPF DO REQUEST, não pelo path param.
		// Isso provavelmente é um bug. Este teste reflete o comportamento atual.
		when(repo.findByCpf("111")).thenReturn(Optional.empty());
		var req = new StudentRequest("Alice", "111", "a@x", "9", "Addr");
		assertThrows(NotFoundException.class, () -> service.update("PATH_IGNORED", req));
		verify(repo, never()).save(any());
	}

	@Test
	void update_updatesUsingRequestCpf_currentImplementation() {
		// Se existir aluno com o CPF do request, ele é atualizado (mesmo que o path seja outro).
		Student existing = new Student();
		existing.setId(7L);
		existing.setCpf("111");
		when(repo.findByCpf("111")).thenReturn(Optional.of(existing));
		when(repo.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

		var req = new StudentRequest("Alice Prime", "111", "a@x", "9", "Addr");
		StudentResponse resp = service.update("PATH_IGNORED", req);

		assertEquals(7L, resp.id());
		assertEquals("Alice Prime", resp.name());
		assertEquals("111", resp.cpf());
		verify(repo).save(existing);
	}

	@Test
	void delete_removesWhenFound() {
		Student s = new Student();
		when(repo.findByCpf("111")).thenReturn(Optional.of(s));

		service.delete("111");
		verify(repo).delete(s);
	}

	@Test
	void delete_throwsWhenNotFound() {
		when(repo.findByCpf("X")).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> service.delete("X"));
		verify(repo, never()).delete(any());
	}
}
