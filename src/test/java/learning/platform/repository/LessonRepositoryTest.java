package learning.platform.repository;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.entity.Course;
import learning.platform.entity.Lesson;
import learning.platform.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Lesson lesson1;
    private Lesson lesson2;

    @BeforeEach
    void setUp() {
        // Creamos curso usando el helper y lo guardamos con su repositorio
        course = TestDataFactory.buildCourse(1L);
        courseRepository.save(course);

        // Creamos lecciones usando el helper y las guardamos
        lesson1 = TestDataFactory.buildLesson(null,
                new LessonCreateRequest(1L, "Lesson 1", "https://example.com/1", null, 1, 10),
                course);
        lesson2 = TestDataFactory.buildLesson(null,
                new LessonCreateRequest(1L, "Lesson 2", "https://example.com/2", null, 2, 15),
                course);

        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
    }

    @Test
    void shouldFindLessonsByCourseOrderedByOrderIndex() {
        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(course);

        assertEquals(2, lessons.size());
        assertEquals("Lesson 1", lessons.get(0).getTitle());
        assertEquals("Lesson 2", lessons.get(1).getTitle());
        assertEquals(1, lessons.get(0).getOrderIndex());
        assertEquals(2, lessons.get(1).getOrderIndex());
    }

    @Test
    void shouldSaveLesson() {
        Lesson newLesson = TestDataFactory.buildLesson(null,
                new LessonCreateRequest(1L, "Lesson 3", "https://example.com/3", null, 3, 20),
                course);
        Lesson savedLesson = lessonRepository.save(newLesson);

        assertNotNull(savedLesson.getId());
        assertEquals("Lesson 3", savedLesson.getTitle());
        assertEquals(3, savedLesson.getOrderIndex());
    }

    @Test
    void shouldFindLessonById() {
        Lesson lesson = lessonRepository.findById(lesson1.getId()).orElseThrow();

        assertEquals("Lesson 1", lesson.getTitle());
        assertEquals(1, lesson.getOrderIndex());
    }

    @Test
    void shouldDeleteLesson() {
        lessonRepository.deleteById(lesson1.getId());

        assertTrue(lessonRepository.findById(lesson1.getId()).isEmpty());
    }

    @Test
    void shouldReturnEmptyListForCourseWithoutLessons() {
        Course newCourse = TestDataFactory.buildCourse(2L);
        courseRepository.save(newCourse);

        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(newCourse);

        assertTrue(lessons.isEmpty());
    }
}
