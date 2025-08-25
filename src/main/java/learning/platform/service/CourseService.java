package learning.platform.service;

import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.entity.Course;
import learning.platform.entity.Enrollment;
import learning.platform.entity.User;
import learning.platform.mapper.CourseMapper;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException; // Para manejar errores
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper; // Inyectamos el Mapper

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseMapper = courseMapper;
    }

    // Lógica para el CRUD
    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO dto, User instructor) {
        // 1. Convertir DTO a Entidad
        Course course = courseMapper.toEntity(dto);
        course.setInstructor(instructor);

        // 2. Guardar la Entidad
        Course savedCourse = courseRepository.save(course);

        // 3. Convertir la Entidad guardada a DTO de respuesta
        return courseMapper.toResponseDTO(savedCourse);
    }

    @Transactional
    public CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO dto) {
        // 1. Buscamos el curso o lanzamos un error si no existe
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        // 2. Usamos el mapper para actualizar los campos
        courseMapper.updateCourseFromDTO(dto, existingCourse);

        // 3. Guardamos la entidad actualizada
        Course updatedCourse = courseRepository.save(existingCourse);

        // 4. Devolvemos el DTO de respuesta
        return courseMapper.toResponseDTO(updatedCourse);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        // 1. Verificamos si el curso existe antes de intentar borrarlo
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found with id: " + courseId);
        }
        // 2. Lo eliminamos
        courseRepository.deleteById(courseId);
    }

    public Course findCourseById(Long id) { return courseRepository.findById(id).orElse(null); }

    // Lógica para listar cursos con filtros y paginación
    public Page<CourseResponseDTO> findAllPublicCourses(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findAll(pageable); // Lógica de filtros iría aquí
        // Convertimos la página de Entidades a una página de DTOs
        return coursePage.map(courseMapper::toResponseDTO);
    }

    // Lógica para la inscripción
    @Transactional
    public Enrollment enrollStudentInCourse(Long courseId, User student) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalStateException("Student is already enrolled in this course.");
        }
        Course course = findCourseById(courseId);
        // Validaciones...
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollmentRepository.save(enrollment);
    }
}