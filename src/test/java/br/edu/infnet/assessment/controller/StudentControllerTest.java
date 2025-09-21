package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.StudentRequest;
import br.edu.infnet.assessment.dto.StudentResponse;
import br.edu.infnet.assessment.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	StudentService studentService;

	StudentResponse sampleResp() {
		return new StudentResponse(1L, "Alice", "01234567890", "alice@email.com", "21999921679", "Endereço completo", OffsetDateTime.now(), null);
	}

	@Test
	void list_shouldReturn200WithArray() throws Exception {
		when(studentService.listAll()).thenReturn(List.of(sampleResp()));

		mvc.perform(get("/api/v1/students"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].cpf").value("01234567890"));
	}

	@Test
	void create_shouldReturn201WithBody() throws Exception {
		when(studentService.create(any(StudentRequest.class))).thenReturn(sampleResp());

		var body = new StudentRequest("Alice", "01234567890", "alice@email.com", "21999921679", "Endereço completo");
		mvc.perform(post("/api/v1/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(body)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Alice"));
	}

	@Test
	void update_shouldReturn201WithBody() throws Exception {
		when(studentService.update(eq("01234567890"), any(StudentRequest.class)))
				.thenReturn(new StudentResponse(1L, "Alice Prime", "01234567890", "alice@email.com", "21999921679", "Endereço completo", OffsetDateTime.now(), null));

		var body = new StudentRequest("Alice Prime", "01234567890", "alice@email.com", "21999921679", "Endereço completo");
		mvc.perform(patch("/api/v1/students/{cpf}", "01234567890")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(body)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Alice Prime"));
	}

	@Test
	void delete_shouldReturn204() throws Exception {
		mvc.perform(delete("/api/v1/students/{cpf}", "01234567890"))
				.andExpect(status().isNoContent());
	}
}
