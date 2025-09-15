package learning.platform.service.impl;

import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.entity.Course;
import learning.platform.entity.User;
import learning.platform.mapper.CourseMapper;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import learning.platform.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper; // Inyectamos el Mapper

    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper, EnrollmentRepository enrollmentRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseMapper = courseMapper;
    }

    // Lógica para el crear un curso
    @Override
    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO dto, User instructor) {
        // 1. Convertir DTO a Entidad
        Course course = courseMapper.toEntity(dto);
        course.setInstructor(instructor);
        course.setSlug(generateSlug(dto.getTitle())); // <-- Generar y asignar el slug
        course.setPublished(false); // Siempre false por defecto
        // 2. Guardar la Entidad
        Course savedCourse = courseRepository.save(course);

        // 3. Convertir la Entidad guardada a DTO de respuesta
        return courseMapper.toResponseDTO(savedCourse);
    }

    /**
     * Actualiza un curso existente.
     */
    @Override
    public CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO dto) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el Curso con ese Id: " + courseId));

        courseMapper.updateCourseFromDTO(dto, existingCourse);
        // Si el título cambia, también deberíamos actualizar el slug
        if (dto.getTitle() != null) {
            existingCourse.setSlug(generateSlug(dto.getTitle()));
        }

        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toResponseDTO(updatedCourse);
    }

    /**
     * Elimina un curso por su ID.
     */
    @Override
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found with id: " + courseId);
        }
        courseRepository.deleteById(courseId);
    }

    //La siguiente función se utiliza para generar la url amigable y guardarla en el slug de la base
    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("\\s+", "-") // Reemplaza espacios con guiones
                .replaceAll("[^a-z0-9-]", "") // Elimina caracteres no alfanuméricos excepto guiones
                .replaceAll("-+", "-"); // Reemplaza múltiples guiones con uno solo
    }

    @Override // <-- Agregar esta anotación
    @Transactional(readOnly = true)
    public Course findCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    // Lógica para listar cursos con filtros y paginación
    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> findAllPublicCourses(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findAll(pageable); // Lógica de filtros iría aquí
        // Convertimos la página de Entidades a una página de DTOs
        return coursePage.map(courseMapper::toResponseDTO);
    }

    /**
     * Busca un curso por su ID y lo devuelve como DTO.
     */
    @Override
    @Transactional(readOnly = true)
    public CourseResponseDTO findCourseDtoById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toResponseDTO) // Convierte la entidad a DTO si la encuentra
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));
    }

    /**
     * Método para que solo el admin pueda cambiar el estado de published
     */
    @Transactional
    public CourseResponseDTO publishCourse(Long courseId, boolean published) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el Id del Curso: " + courseId));
        course.setPublished(published);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponseDTO(updatedCourse);
    }

    // Lógica para la inscripción
    /*
    @Transactional
    public Enrollment enrollStudentInCourse(Long courseId, User student) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalStateException("El estudiante actualmente está inscrito en este curso.");
        }
        Course course = findCourseById(courseId);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollmentRepository.save(enrollment);
    }

     */
}