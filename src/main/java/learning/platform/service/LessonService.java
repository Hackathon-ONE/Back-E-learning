package learning.platform.service;

import learning.platform.dto.lesson.LessonCreateRequest;
import learning.platform.dto.lesson.LessonResponse;

import java.util.List;

public interface LessonService {
    LessonResponse createLesson(LessonCreateRequest request);
    List<LessonResponse> getLessonsByCourse(Integer courseId);
    LessonResponse updateLesson(Integer id, LessonCreateRequest request);
    void deleteLesson(Integer id);
    void reorderLessons(Integer courseId, List<Integer> newOrder);
}


