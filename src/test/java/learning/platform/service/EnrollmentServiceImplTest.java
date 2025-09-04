package learning.platform.service;

import learning.platform.dto.EnrollmentRequest;
import learning.platform.dto.EnrollmentResponse;
import learning.platform.entity.Course;
import learning.platform.entity.Enrollment;
import learning.platform.entity.User;
import learning.platform.exception.ResourceNotFoundException;
import learning.platform.mapper.EnrollmentMapper;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.repository.UserRepository;
import learning.platform.service.impl.EnrollmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository repository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EnrollmentMapper mapper;

    @InjectMocks
    private EnrollmentServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enrollStudent_success() {
        Long courseId = 10L;
        Long studentId = 20L;

        // <-- CORRECCIÓN: primero studentId, luego courseId
        EnrollmentRequest request = new EnrollmentRequest(studentId, courseId);

        User student = new User();
        student.setId(studentId);
        student.setFullName("Alumno");

        Course course = new Course(); // adapta si tu entidad necesita constructor con args
        Enrollment enrollment = new Enrollment(request, course, student);
        EnrollmentResponse response = new EnrollmentResponse(enrollment);

        // stubs: usamos matchers para evitar mismatch de argumentos
        when(repository.findByCourseIdAndStudentId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(courseRepository.findById(eq(courseId))).thenReturn(Optional.of(course));
        when(userRepository.findById(eq(studentId))).thenReturn(Optional.of(student));
        when(repository.save(any(Enrollment.class))).thenReturn(enrollment);
        when(mapper.toResponse(any(Enrollment.class))).thenReturn(response);

        // execute
        EnrollmentResponse r = service.enrollStudent(request);

        // assertions
        assertNotNull(r);
        verify(repository, times(1)).findByCourseIdAndStudentId(eq(courseId), eq(studentId));
        verify(courseRepository, times(1)).findById(eq(courseId));
        verify(userRepository, times(1)).findById(eq(studentId));
        verify(repository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void enrollStudent_alreadyExists_throws() {
        Long courseId = 10L;
        Long studentId = 20L;

        // <-- CORRECCIÓN: primero studentId, luego courseId
        EnrollmentRequest request = new EnrollmentRequest(studentId, courseId);

        Enrollment existing = mock(Enrollment.class);

        // forzamos el repo a devolver Optional.of(existing)
        when(repository.findByCourseIdAndStudentId(eq(courseId), eq(studentId))).thenReturn(Optional.of(existing));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.enrollStudent(request));
        assertEquals("Ya inscrito", ex.getMessage());

        // confirmamos que el flujo se detuvo y no se llamó a otros repos
        verify(courseRepository, never()).findById(any());
        verify(userRepository, never()).findById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void enrollStudent_courseNotFound_throws() {
        Long courseId = 10L;
        Long studentId = 20L;

        // <-- CORRECCIÓN: primero studentId, luego courseId
        EnrollmentRequest request = new EnrollmentRequest(studentId, courseId);

        when(repository.findByCourseIdAndStudentId(eq(courseId), eq(studentId))).thenReturn(Optional.empty());
        when(courseRepository.findById(eq(courseId))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.enrollStudent(request));
        verify(repository, never()).save(any());
    }
}
