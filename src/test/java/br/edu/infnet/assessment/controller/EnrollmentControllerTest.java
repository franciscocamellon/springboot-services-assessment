package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.EnrollmentRequest;
import br.edu.infnet.assessment.dto.GradeRequest;
import br.edu.infnet.assessment.model.Course;
import br.edu.infnet.assessment.model.Enrollment;
import br.edu.infnet.assessment.model.Student;
import br.edu.infnet.assessment.service.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EnrollmentController.class)
class EnrollmentControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	EnrollmentService service;

	Enrollment sampleEnrollment() {
		Student student = new Student();
		student.setName("John Doe");
		student.setCpf("123456789");
		student.setEmail("john.doe@example.com");
		student.setPhone("999");
		student.setAddress("address");

		Course course = new Course();
		course.setName("Desenvolvimento de Serviços com SpringBoot");
		course.setCode("DR1");

		Enrollment enrollment = new Enrollment();
		enrollment.setStudent(student);
		enrollment.setCourse(course);
		enrollment.setId(42L);
		enrollment.setGrade(8.5);

		return enrollment;
	}

	@Test
	void list_shouldReturn200WithArray() throws Exception {
		when(service.listAll()).thenReturn(List.of(sampleEnrollment()));

		mvc.perform(get("/api/enrollments"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(42));
	}

	@Test
	void enroll_shouldReturn200WithId() throws Exception {
		when(service.enroll(any(EnrollmentRequest.class))).thenReturn(99L);

		var body = new EnrollmentRequest(1L, 2L);
		mvc.perform(post("/api/enrollments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(body)))
				.andExpect(status().isOk())
				.andExpect(content().string("99"));
	}

	@Test
	void assignGrade_shouldReturn200() throws Exception {
		doNothing().when(service).assignGrade(eq(99L), any(GradeRequest.class));

		var body = new GradeRequest(9.3);
		mvc.perform(put("/api/enrollments/{id}/grade", 99L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(body)))
				.andExpect(status().isOk());

		verify(service).assignGrade(eq(99L), ArgumentMatchers.any());
	}

	@Test
	void withdrawal_shouldReturn200() throws Exception {
		doNothing().when(service).withdrawal(77L);

		mvc.perform(delete("/api/enrollments/{id}", 77L))
				.andExpect(status().isOk());

		verify(service).withdrawal(77L);
	}
}
