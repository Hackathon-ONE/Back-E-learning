package learning.platform.service.impl;

import learning.platform.dto.StudentCourseProgressDTO;
import learning.platform.entity.Course;
import learning.platform.entity.Enrollment;
import learning.platform.exception.ResourceNotFoundException;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.service.InstructorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class InstructorServiceImpl implements InstructorService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    public InstructorServiceImpl(CourseRepository courseRepository,
                                 EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentCourseProgressDTO> getStudentsByInstructor(Long instructorId) {
        boolean instructorExists = courseRepository.existsByInstructor_Id(instructorId);
        if (!instructorExists) {
            throw new ResourceNotFoundException("Instructor no encontrado");
        }

        List<Course> courses = courseRepository.findByInstructorId(instructorId);
        if (courses.isEmpty()) {
            return List.of(); // Instructor sin cursos asignados
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseIn(courses);

        return enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::getStudent))
                .entrySet()
                .stream()
                .map(entry -> {
                    var student = entry.getKey();
                    var coursesProgress = entry.getValue().stream()
                            .map(enrollment -> {
                                var c = new StudentCourseProgressDTO.CourseProgressDTO();
                                c.setId(enrollment.getCourse().getId());
                                c.setTitle(enrollment.getCourse().getTitle());
                                c.setProgressPercent(Optional.of(enrollment.getProgressPercent()).orElse(0));

                                return c;
                            })
                            .collect(Collectors.toList());

                    var dto = new StudentCourseProgressDTO();
                    dto.setId(student.getId());
                    dto.setFullName(student.getFullName());
                    dto.setEmail(student.getEmail());
                    dto.setCoursesEnrolled(coursesProgress);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}