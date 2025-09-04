package learning.platform.testutil;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.entity.Course;
import learning.platform.entity.Lesson;

public class TestDataFactory {

    /**
     * Construye una lección consistente para tests.
     *
     * @param id      ID de la lección (puede ser null)
     * @param request DTO con los datos de la lección
     * @param course  Curso asociado
     * @return Objeto Lesson listo para persistir o usar en mocks
     */
    public static Lesson buildLesson(Long id, LessonCreateRequest request, Course course) {
        Lesson lesson = new Lesson();
        lesson.setId(id);
        lesson.setCourse(course);
        lesson.setTitle(request.title());        // ⚠️ getter de record
        lesson.setContentUrl(request.contentUrl()); // ⚠️ getter de record
        lesson.setOrderIndex(request.orderIndex()); // ⚠️ getter de record
        return lesson;
    }

    /**
     * Construye un Course simple con un ID.
     *
     * @param id ID del curso
     * @return Curso
     */
    public static Course buildCourse(Long id) {
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
