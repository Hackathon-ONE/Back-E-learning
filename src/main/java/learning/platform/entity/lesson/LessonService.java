import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

public interface LessonService {
    LessonResponse createLesson(LessonCreateRequest request);
    List<LessonResponse> getLessonsByCourse(Integer courseId);
    LessonResponse updateLesson(Integer id, LessonCreateRequest request);
    void deleteLesson(Integer id);
    void reorderLessons(Integer courseId, List<Integer> newOrder);
}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonMapper lessonMapper;

    @Override
    @Transactional
    public LessonResponse createLesson(LessonCreateRequest request) {
        Lesson lesson = lessonMapper.toEntity(request);
        lesson = lessonRepository.save(lesson);
        return lessonMapper.toResponse(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByCourse(Integer courseId) {
        // Assuming Course entity is available or fetched by courseId
        Course course = new Course(); // Replace with actual course fetching logic
        course.setId(courseId);
        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(course);
        return lessons.stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(Integer id, LessonCreateRequest request) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(id);
        if (optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();
            lessonMapper.updateEntityFromRequest(lesson, request);
            lesson = lessonRepository.save(lesson);
            return lessonMapper.toResponse(lesson);
        }
        throw new RuntimeException("Lesson not found");
    }

    @Override
    @Transactional
    public void deleteLesson(Integer id) {
        lessonRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void reorderLessons(Integer courseId, List<Integer> newOrder) {
        // Assuming Course entity is available or fetched by courseId
        Course course = new Course(); // Replace with actual course fetching logic
        course.setId(courseId);
        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(course);
        for (int i = 0; i < newOrder.size() && i < lessons.size(); i++) {
            lessons.get(i).setOrderIndex(newOrder.get(i));
        }
        lessonRepository.saveAll(lessons);
    }
}
