package learning.platform.repository;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.entity.Course;
import learning.platform.enums.ContentType;
import learning.platform.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProgressRepositoryTest {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Enrollment enrollment;
    private Lesson lesson;
    private Progress progress;

    @BeforeEach
    void setUp() {
        // Crear y persistir inscripción
        enrollment = TestDataFactory.buildEnrollment(null);
        entityManager.persist(enrollment);

        // Crear y persistir curso y lección directamente
        Course course = TestDataFactory.buildCourse(null);
        entityManager.persist(course);

        LessonCreateRequest lessonRequest = new LessonCreateRequest(
                course.getId(),       // courseId
                "título-prueba",      // title
                "https://dummy.url",  // contentUrl
                ContentType.VIDEO,    // contentType
                1,                    // orderIndex
                30                    // durationMinutes
        );

        Lesson lesson = TestDataFactory.buildLesson(null, lessonRequest, course);
        entityManager.persist(lesson);


        // Crear y persistir progreso
        progress = new Progress();
        progress.setEnrollment(enrollment);
        progress.setLesson(lesson);
        progress.setCompleted(true);
        progress.setScore(85);
        progress.setCompletionPercentage(BigDecimal.valueOf(100.00));
        progress.setUpdatedAt(Instant.now());

        entityManager.persist(progress);
        entityManager.flush();
    }

    @Test
    void shouldFindByEnrollment() {
        List<Progress> progresses = progressRepository.findByEnrollment(enrollment);

        assertEquals(1, progresses.size());

        Progress found = progresses.get(0);
        assertTrue(found.getCompleted());
        assertEquals(85, found.getScore());
        assertEquals(BigDecimal.valueOf(100.00), found.getCompletionPercentage());
        assertNotNull(found.getUpdatedAt());
    }

    @Test
    void shouldFindByEnrollmentAndLesson() {
        Optional<Progress> progressOpt = progressRepository.findByEnrollmentAndLesson(enrollment, lesson);

        assertTrue(progressOpt.isPresent());

        Progress found = progressOpt.get();
        assertTrue(found.getCompleted());
        assertEquals(85, found.getScore());
        assertEquals(BigDecimal.valueOf(100.00), found.getCompletionPercentage());
        assertNotNull(found.getUpdatedAt());
    }
}
