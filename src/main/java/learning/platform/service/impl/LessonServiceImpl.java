package learning.platform.service.impl;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.dto.LessonResponse;
import learning.platform.entity.Course;
import learning.platform.entity.Lesson;
import learning.platform.mapper.LessonMapper;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.LessonRepository;
import learning.platform.service.LessonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    public LessonServiceImpl(LessonRepository lessonRepository,
                             CourseRepository courseRepository,
                             LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public LessonResponse createLesson(LessonCreateRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + request.courseId()));

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);

        Lesson saved = lessonRepository.save(lesson);
        return lessonMapper.toResponse(saved);
    }

    @Override
    public LessonResponse getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con ID: " + id));
        return lessonMapper.toResponse(lesson);
    }

    @Override
    public List<LessonResponse> getLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + courseId));

        return lessonRepository.findByCourseOrderByOrderIndex(course)
                .stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LessonResponse updateLesson(Long id, LessonCreateRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con ID: " + id));

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + request.courseId()));

        lesson.setTitle(request.title());
        lesson.setOrderIndex(request.orderIndex());
        lesson.setDurationSeconds(request.durationSeconds());
        lesson.setCourse(course);

        Lesson updated = lessonRepository.save(lesson);
        return lessonMapper.toResponse(updated);
    }

    @Override
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new IllegalArgumentException("Lección no encontrada con ID: " + id);
        }
        lessonRepository.deleteById(id);
    }

    @Override
    public List<LessonResponse> reorderLessons(Long courseId, List<Long> newOrder) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + courseId));

        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(course);

        Set<Long> lessonIds = lessons.stream()
                .map(Lesson::getId)
                .collect(Collectors.toSet());
        Set<Long> newOrderIds = new HashSet<>(newOrder);

        if (!lessonIds.equals(newOrderIds)) {
            throw new IllegalArgumentException("La lista de IDs proporcionada no coincide con las lecciones del curso");
        }

        for (int i = 0; i < newOrder.size(); i++) {
            Long lessonId = newOrder.get(i);
            Lesson lesson = lessons.stream()
                    .filter(l -> l.getId().equals(lessonId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con ID: " + lessonId));
            lesson.setOrderIndex(i + 1);
        }

        lessonRepository.saveAll(lessons);

        // Devolver en el nuevo orden
        return newOrder.stream()
                .map(id -> lessons.stream()
                        .filter(l -> l.getId().equals(id))
                        .findFirst()
                        .orElseThrow())
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }
}
