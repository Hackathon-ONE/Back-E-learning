package learning.platform.service;

import learning.platform.dto.StudentCourseProgressDTO;
import learning.platform.entity.Course;
import learning.platform.entity.Enrollment;
import learning.platform.entity.User;
import learning.platform.exception.ResourceNotFoundException;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.service.impl.InstructorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServiceTest {

    private CourseRepository courseRepository;
    private EnrollmentRepository enrollmentRepository;
    private InstructorService instructorService;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        enrollmentRepository = mock(EnrollmentRepository.class);
        instructorService = new InstructorServiceImpl(courseRepository, enrollmentRepository);
    }

    @Test
    void shouldReturnStudentsWhenInstructorExists() {
        Long instructorId = 1L;

        User student = new User();
        student.setId(10L);
        student.setFullName("Juan Pérez");
        student.setEmail("juan@test.com");

        Course course = new Course();
        course.setId(100L);
        course.setTitle("Java Básico");

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setProgressPercent(50);

        when(courseRepository.existsByInstructor_Id(instructorId)).thenReturn(true);
        when(courseRepository.findByInstructorId(instructorId)).thenReturn(List.of(course));
        when(enrollmentRepository.findByCourseIn(List.of(course))).thenReturn(List.of(enrollment));

        List<StudentCourseProgressDTO> result = instructorService.getStudentsByInstructor(instructorId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getFullName());
        assertEquals(1, result.get(0).getCoursesEnrolled().size());
        assertEquals(50, result.get(0).getCoursesEnrolled().get(0).getProgressPercent());
    }

    @Test
    void shouldThrowExceptionWhenInstructorNotFound() {
        Long instructorId = 99L;
        when(courseRepository.existsByInstructor_Id(instructorId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> instructorService.getStudentsByInstructor(instructorId));
    }
}