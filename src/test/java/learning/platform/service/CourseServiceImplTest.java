package learning.platform.service;

import learning.platform.dto.CourseCreateRequest;
import learning.platform.dto.CourseResponse;
import learning.platform.entity.Course;
import learning.platform.entity.User;
import learning.platform.enums.Category;
import learning.platform.mapper.CourseMapper;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CoursServiceImpl courseService;

    private User instructor;
    private CourseCreateRequest request;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        instructor = new User();
        instructor.setId(1L);
        instructor.setName("Juan Perez");
        instructor.setEmail("juan@example.com");

        request = new CourseCreateRequest("Java Basics", "Curso de Java", Category.TECH, 1L);

        course = new Course(request, instructor);
    }

    @Test
    void createCourse_success() {
        // Mock behaviors
        when(userRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseMapper.toEntity(request, instructor)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toResponse(course)).thenReturn(new CourseResponse(course));

        // Execute
        CourseResponse response = courseService.create(request);

        // Verify
        assertNotNull(response);
        assertEquals("Java Basics", response.title());
        assertEquals("Curso de Java", response.description());
        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void createCourse_instructorNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.create(request);
        });

        assertEquals("Instructor not found", exception.getMessage());
        verify(courseRepository, never()).save(any());
    }
}
