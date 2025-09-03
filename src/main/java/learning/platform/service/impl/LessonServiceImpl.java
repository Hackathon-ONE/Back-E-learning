package learning.platform.service.impl;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.dto.LessonResponse;
import learning.platform.entity.Lesson;
import learning.platform.mapper.LessonMapper;
import learning.platform.repository.LessonRepository;
import learning.platform.service.LessonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la gestión de lecciones.
 */
@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.lessonMapper = lessonMapper;
    }

    /**
     * Constructor para inyección de dependencias.
     *
     * @param lessonRepository Repositorio de lecciones.
     * @param courseRepository Repositorio de cursos.
     * @param lessonMapper     Mapeador para convertir entre entidades y DTOs.


    public LessonServiceImpl(LessonRepository lessonRepository,
                             CourseRepository courseRepository,
                             LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.lessonMapper = lessonMapper;
    }

    /**
     * Crea una nueva lección a partir de un DTO de solicitud.
     *
     * @param request DTO con los datos de la lección
     * @return DTO de respuesta con los datos de la lección creada
     * @throws IllegalArgumentException si el curso no existe
     */
    @Override
    public LessonResponse createLesson(LessonCreateRequest request) {
        // Buscar el curso
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + request.courseId()));

        // Convertir DTO a entidad
        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);

        // Guardar en la base de datos
        Lesson saved = lessonRepository.save(lesson);

        // Convertir a DTO de respuesta
        return lessonMapper.toResponse(saved);
    }

    /**
     * Obtiene todas las lecciones asociadas a un curso, ordenadas por orderIndex.
     *
     * @param courseId ID del curso
     * @return Lista de DTOs de respuesta con las lecciones
     * @throws IllegalArgumentException si el curso no existe
     */
    @Override
    public List<LessonResponse> getLessonsByCourse(Long courseId) {
        // Buscar el curso
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + courseId));

        // Obtener lecciones ordenadas por orderIndex
        return lessonRepository.findByCourseOrderByOrderIndex(course)
                .stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una lección existente.
     *
     * @param id      ID de la lección a actualizar
     * @param request DTO con los nuevos datos de la lección
     * @return DTO de respuesta con los datos de la lección actualizada
     * @throws IllegalArgumentException si la lección o el curso no existen
     */
    @Override
    public LessonResponse updateLesson(Long id, LessonCreateRequest request) {
        // Buscar la lección:
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con ID: " + id));

        // Buscar el curso:
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + request.courseId()));

        // Actualizar campos de la lección:
        lesson.setTitle(request.title());
        lesson.setContentUrl(request.contentUrl());
        lesson.setContentType(request.contentType());
        lesson.setOrderIndex(request.orderIndex());
        lesson.setDuration(request.duration());
        lesson.setCourse(course);

        // Guardar los cambios:
        Lesson updated = lessonRepository.save(lesson);
        return lessonMapper.toResponse(updated);
    }

    /**
     * Elimina una lección por su ID.
     *
     * @param id ID de la lección a eliminar
     * @throws IllegalArgumentException si la lección no existe
     */
    @Override
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new IllegalArgumentException("Lección no encontrada con ID: " + id);
        }
        lessonRepository.deleteById(id);
    }

    /**
     * Reordena las lecciones de un curso según una lista de IDs.
     *
     * @param courseId ID del curso
     * @param newOrder Lista de IDs de lecciones en el nuevo orden
     * @return
     * @throws IllegalArgumentException si el curso no existe, los IDs son inválidos o no coinciden con las lecciones del curso
     */
    @Override
    public List<LessonResponse> reorderLessons(Long courseId, List<Long> newOrder) {
        // Buscar el curso
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + courseId));

        // Obtener todas las lecciones del curso
        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(course);

        // Validar que newOrder contiene exactamente los mismos IDs que las lecciones del curso
        Set<Long> lessonIds = lessons.stream()
                .map(Lesson::getId)
                .collect(Collectors.toSet());
        Set<Long> newOrderIds = new HashSet<>(newOrder);

        if (!lessonIds.equals(newOrderIds)) {
            throw new IllegalArgumentException("La lista de IDs proporcionada no coincide con las lecciones del curso");
        }

        // Actualizar orderIndex según el nuevo orden
        for (int i = 0; i < newOrder.size(); i++) {
            Long lessonId = newOrder.get(i);
            Lesson lesson = lessons.stream()
                    .filter(l -> l.getId().equals(lessonId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con ID: " + lessonId));
            lesson.setOrderIndex(i + 1);
            lessonRepository.save(lesson);
        }
        return null;
    }
}