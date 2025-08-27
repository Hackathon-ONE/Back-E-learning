package learning.platform.service.impl;

import learning.platform.dto.lesson.LessonCreateRequest;
import learning.platform.dto.lesson.LessonResponse;
import learning.platform.entity.Lesson;
import learning.platform.mapper.LessonMapper;
import learning.platform.repository.LessonRepository;
import learning.platform.service.LessonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        // Convertimos DTO a entidad sin course
        Lesson lesson = lessonMapper.toEntity(request);

        // Buscamos el course y lo asignamos
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        lesson.setCourse(course);

        // Guardamos en DB
        Lesson saved = lessonRepository.save(lesson);

        // Convertimos a DTO de respuesta
        return lessonMapper.toResponse(saved);
    }

    @Override
    public List<LessonResponse> getLessonsByCourse(Integer courseId) {
        return lessonRepository.findByCourseOrderByOrderIndex(courseId)
                .stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LessonResponse updateLesson(Integer id, LessonCreateRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // Actualizamos la entidad desde el DTO
        lessonMapper.updateEntityFromRequest(lesson, request);

        // Si el course cambió
        if (!lesson.getCourse().getId().equals(request.getCourseId())) {
            Course newCourse = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            lesson.setCourse(newCourse);
        }

        // Guardamos los cambios
        Lesson updated = lessonRepository.save(lesson);
        return lessonMapper.toResponse(updated);
    }

    @Override
    public void deleteLesson(Integer id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public void reorderLessons(Integer courseId, List<Integer> newOrder) {
        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(courseId);

        for (int i = 0; i < newOrder.size(); i++) {
            int lessonId = newOrder.get(i);
            Lesson lesson = lessons.stream()
                    .filter(l -> l.getId().equals((long)lessonId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));
            lesson.setOrderIndex(i + 1);
            lessonRepository.save(lesson);
        }
    }
}
