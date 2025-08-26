package learning.platform.service;

import learning.platform.dto.EnrollmentRequest;
import learning.platform.dto.EnrollmentResponse;
import learning.platform.entity.Enrollment;
import learning.platform.enums.EnrollmentStatus;
import learning.platform.exception.ResourceNotFoundException;
import learning.platform.mapper.EnrollmentMapper;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements EnrollmentService{
    private final EnrollmentRepository repository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentMapper mapper;

    public EnrollmentServiceImpl(EnrollmentRepository repository, CourseRepository courseRepository, UserRepository userRepository, EnrollmentMapper mapper) {
        this.repository = repository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public EnrollmentResponse enrollStudent(EnrollmentRequest request) {
        //validacion de no duplicado
        repository.findByCourseIdAndStudentId(request.courseId(), request.studentId())
                .ifPresent(e -> { throw new IllegalStateException("Ya inscrito"); });
        //validando que existan estudiante y curso
        var course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));
        var student = userRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        var enrollment = new Enrollment(request, course, student);

        return mapper.toResponse(repository.save(enrollment));
    }

    @Override
    public Page<EnrollmentResponse> findByStudent(Long studentId, Pageable pageable) {
        return repository.findByStudentId(studentId, pageable).map(EnrollmentResponse::new);
    }

    @Override
    public Page<EnrollmentResponse> findByCourse(Long courseId, Pageable pageable) {
        return repository.findByCourseId(courseId,pageable).map(EnrollmentResponse::new);
    }

    @Override
    public EnrollmentResponse completeCourse(Long enrollmentId) {
        Enrollment enrollment = repository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment no encontrado"));
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        repository.save(enrollment);

        return new EnrollmentResponse(enrollment);
    }


    @Override
    public EnrollmentResponse cancelCourse(Long enrollmentId) {
        Enrollment enrollment = repository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment no encontrado"));
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        repository.save(enrollment);

        return new EnrollmentResponse(enrollment);
    }

    public void deleteEnrollment(Long id) {
        repository.deleteById(id);
    }
}


