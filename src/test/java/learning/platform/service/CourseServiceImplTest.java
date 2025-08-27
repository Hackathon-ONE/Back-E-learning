package learning.platform.service;

import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.entity.Course;
import learning.platform.mapper.CourseMapper;
import learning.platform.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    @Test
    void testCreateCourse() {
        // --- 1. Arrange (Preparar) ---

        // Datos de entrada
        User instructor = new User();
        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setTitle("New Course");

        // Objetos intermedios que esperamos
        Course courseToSave = new Course(); // El objeto que el mapper creará
        courseToSave.setInstructor(instructor);

        Course savedCourse = new Course(); // El objeto que el repositorio devolverá
        savedCourse.setId(1L);
        savedCourse.setInstructor(instructor);

        CourseResponseDTO expectedResponse = new CourseResponseDTO(); // El DTO final
        expectedResponse.setId(1L);
        expectedResponse.setTitle("New Course");

        // --- Simulación explícita de cada paso ---
        // a) Cuando el mapper convierta el DTO, devuelve 'courseToSave'
        when(courseMapper.toEntity(requestDTO)).thenReturn(courseToSave);

        // b) Cuando el repositorio guarde esa entidad, devuelve 'savedCourse'
        when(courseRepository.save(courseToSave)).thenReturn(savedCourse);

        // c) Cuando el mapper convierta la entidad guardada, devuelve 'expectedResponse'
        when(courseMapper.toResponseDTO(savedCourse)).thenReturn(expectedResponse);

        // --- 2. Act (Actuar) ---
        CourseResponseDTO result = courseServiceImpl.createCourse(requestDTO, instructor);

        // --- 3. Assert (Afirmar) ---
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Course", result.getTitle());

        // Opcional: Verifica que cada mock fue llamado exactamente una vez
        verify(courseMapper, times(1)).toEntity(requestDTO);
        verify(courseRepository, times(1)).save(courseToSave);
        verify(courseMapper, times(1)).toResponseDTO(savedCourse);
    }

    @Test
    void testUpdateCourse() {
        // Arrange
        Long courseId = 1L;
        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setTitle("Updated Title");

        Course existingCourse = new Course();
        existingCourse.setId(courseId);
        existingCourse.setTitle("Old Title");

        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setTitle("Updated Title");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(existingCourse);
        when(courseMapper.toResponseDTO(any(Course.class))).thenReturn(responseDTO);

        // Act
        CourseResponseDTO result = courseServiceImpl.updateCourse(courseId, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testDeleteCourse() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.existsById(courseId)).thenReturn(true);
        // Como deleteById no devuelve nada, usamos doNothing()
        doNothing().when(courseRepository).deleteById(courseId);

        // Act
        courseServiceImpl.deleteCourse(courseId);

        // Assert
        // Verificamos que el método deleteById fue llamado exactamente una vez con el ID correcto
        verify(courseRepository, times(1)).deleteById(courseId);
    }
}