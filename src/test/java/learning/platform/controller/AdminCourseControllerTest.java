package learning.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminCourseController.class)
class AdminCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CourseServiceImpl courseService;

    private ObjectMapper objectMapper;
    private CourseResponseDTO courseResponseDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        courseResponseDTO = new CourseResponseDTO();
        courseResponseDTO.setId(1L);
        courseResponseDTO.setTitle("Java Básico");
        courseResponseDTO.setDescription("Curso de Java para principiantes");
    }

    @Test
    void shouldGetAllCourses() throws Exception {
        Page<CourseResponseDTO> page = new PageImpl<>(List.of(courseResponseDTO));
        when(courseService.findAllPublicCourses(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/admin/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(courseResponseDTO.getId()))
                .andExpect(jsonPath("$.content[0].title").value(courseResponseDTO.getTitle()));
    }

    @Test
    void shouldUpdateCourse() throws Exception {
        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setTitle("Java Avanzado");
        requestDTO.setDescription("Curso avanzado de Java");

        CourseResponseDTO updatedDTO = new CourseResponseDTO();
        updatedDTO.setId(1L);
        updatedDTO.setTitle(requestDTO.getTitle());
        updatedDTO.setDescription(requestDTO.getDescription());


        mockMvc.perform(put("/api/admin/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDTO.getId()))
                .andExpect(jsonPath("$.title").value(updatedDTO.getTitle()));
    }

    @Test
    void shouldDeleteCourse() throws Exception {
//        doNothing().when(courseService).deleteCourse(1l);

        mockMvc.perform(delete("/api/admin/courses/1"))
                .andExpect(status().isNoContent());
    }
}