package learning.platform.testutil;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.entity.Course;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;

import java.lang.reflect.Field;
import java.time.Instant;

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

    // ---------- NUEVO PARA PROGRESS ----------

    /**
     * Construye una Enrollment simple con un ID.
     */
    public static Enrollment buildEnrollment(Long id) {
        Enrollment enrollment = new Enrollment();
        try {
            Field idField = Enrollment.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(enrollment, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error setting Enrollment id", e);
        }
        return enrollment;
    }


    /**
     * Construye una Lesson simple con un ID (sin DTO, sólo para tests).
     */
    public static Lesson buildLessonWithId(Long id) {
        Lesson lesson = new Lesson();
        lesson.setId(id);
        return lesson;
    }

    /**
     * Construye un Progress consistente para tests.
     */
    public static Progress buildProgress(Enrollment enrollment, Lesson lesson, boolean completed, Integer score) {
        Progress progress = new Progress();
        progress.setEnrollment(enrollment);
        progress.setLesson(lesson);
        progress.setCompleted(completed);
        progress.setScore(score);
        progress.setUpdatedAt(Instant.now());
        return progress;
    }
}
