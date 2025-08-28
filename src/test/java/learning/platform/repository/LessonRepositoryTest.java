package learning.platform.repository;

import learning.platform.entity.Course;
import learning.platform.entity.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de integración para LessonRepository.
 */
@DataJpaTest
public class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Course course;

    /**
     * Configura los datos de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        entityManager.persist(course);

        Lesson lesson1 = new Lesson();
        lesson1.setCourse(course);
        lesson1.setTitle("Lesson 1");
        lesson1.setOrderIndex(1);
        entityManager.persist(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setCourse(course);
        lesson2.setTitle("Lesson 2");
        lesson2.setOrderIndex(2);
        entityManager.persist(lesson2);
    }

    /**
     * Verifica que las lecciones se devuelvan ordenadas por orderIndex.
     */
    @Test
    void shouldFindLessonsByCourseOrderedByOrderIndex() {
        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(course);

        assertEquals(2, lessons.size());
        assertEquals("Lesson 1", lessons.get(0).getTitle());
        assertEquals("Lesson 2", lessons.get(1).getTitle());
        assertEquals(1, lessons.get(0).getOrderIndex());
        assertEquals(2, lessons.get(1).getOrderIndex());
    }

    /**
     * Verifica que se guarde una lección correctamente.
     */
    @Test
    void shouldSaveLesson() {
        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle("Lesson 3");
        lesson.setOrderIndex(3);

        Lesson savedLesson = lessonRepository.save(lesson);

        assertNotNull(savedLesson.getId());
        assertEquals("Lesson 3", savedLesson.getTitle());
        assertEquals(3, savedLesson.getOrderIndex());
    }

    /**
     * Verifica que se encuentre una lección por ID.
     */
    @Test
    void shouldFindLessonById() {
        Lesson lesson = lessonRepository.findById(1L).orElseThrow();

        assertEquals("Lesson 1", lesson.getTitle());
        assertEquals(1, lesson.getOrderIndex());
    }

    /**
     * Verifica que se elimine una lección correctamente.
     */
    @Test
    void shouldDeleteLesson() {
        lessonRepository.deleteById(1L);

        assertTrue(lessonRepository.findById(1L).isEmpty());
    }

    /**
     * Verifica que un curso sin lecciones devuelva una lista vacía.
     */
    @Test
    void shouldReturnEmptyListForCourseWithoutLessons() {
        Course newCourse = new Course();
        newCourse.setId(2L);
        entityManager.persist(newCourse);

        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(newCourse);

        assertTrue(lessons.isEmpty());
    }
}