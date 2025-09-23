package br.edu.infnet.assessment.controller;

import br.edu.infnet.assessment.dto.CourseRequest;
import br.edu.infnet.assessment.dto.CourseResponse;
import br.edu.infnet.assessment.entities.Course;
import br.edu.infnet.assessment.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
class CourseControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper om;

	@MockBean
	CourseService courseService;

	CourseResponse sampleCourse() {
		return new CourseResponse(10L, "Mathematics", "MATH1", OffsetDateTime.now(), null);
	}

	@Test
	void list_shouldReturn200WithArray() throws Exception {
		Course newCourse = new Course();
		newCourse.setId(1L);
		newCourse.setName("Mathematics");
		newCourse.setCode("MATH1");

		when(courseService.listAll()).thenReturn(List.of(newCourse));

		mvc.perform(get("/api/v1/courses"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].code").value("MATH1"));
	}

	@Test
	void create_shouldReturn201WithBody() throws Exception {
		when(courseService.create(any(CourseRequest.class))).thenReturn(sampleCourse());

		var body = new CourseRequest("Mathematics", "MATH1");
		mvc.perform(post("/api/v1/courses")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(body)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").value("MATH1"));
	}

	@Test
	void update_shouldReturn201WithBody() throws Exception {
		when(courseService.updateCourse(eq("MATH1"), any(CourseRequest.class)))
				.thenReturn(new CourseResponse(10L, "Math II", "MATH1", OffsetDateTime.now(), null));

		var body = new CourseRequest("Math II", "MATH1");
		mvc.perform(patch("/api/v1/courses/{code}", "MATH1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(body)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Math II"));
	}

	@Test
	void delete_shouldReturn204() throws Exception {
		mvc.perform(delete("/api/v1/courses/{code}", "MATH1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void approved_shouldReturn201WithArray() throws Exception {
		when(courseService.approvedStudents("MATH1")).thenReturn(List.of("Alice", "Bob"));

		mvc.perform(get("/api/v1/courses/{code}/approved", "MATH1"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$[0]").value("Alice"))
				.andExpect(jsonPath("$[1]").value("Bob"));
	}

	@Test
	void failed_shouldReturn201WithArray() throws Exception {
		when(courseService.failedStudents("MATH1")).thenReturn(List.of("Carol"));

		mvc.perform(get("/api/v1/courses/{code}/failed", "MATH1"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$[0]").value("Carol"));
	}
}
