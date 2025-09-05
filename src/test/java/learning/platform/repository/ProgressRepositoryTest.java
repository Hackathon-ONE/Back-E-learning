package learning.platform.repository;

import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.testutil.TestDataFactory;
import learning.platform.dto.LessonCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProgressRepositoryTest {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Enrollment enrollment;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        // Creamos una inscripción y la persistimos
        enrollment = TestDataFactory.buildEnrollment(null); // id se genera automáticamente
        entityManager.persist(enrollment);

        // Creamos un curso y una lección usando el record LessonCreateRequest
        var lessonRequest = new LessonCreateRequest("1", "título-prueba", Integer.valueOf(1));
        lesson = TestDataFactory.buildLesson(null, lessonRequest, TestDataFactory.buildCourse(null));
        entityManager.persist(lesson);

        // Creamos un Progress asociado
        Progress progress = TestDataFactory.buildProgress(enrollment, lesson, true, 85);
        entityManager.persist(progress);
    }

    @Test
    void shouldFindByEnrollment() {
        List<Progress> progresses = progressRepository.findByEnrollment(enrollment);
        assertEquals(1, progresses.size());
        assertTrue(progresses.get(0).getCompleted());
        assertEquals(85, progresses.get(0).getScore());
    }

    @Test
    void shouldFindByEnrollmentAndLesson() {
        Optional<Progress> progressOpt = progressRepository.findByEnrollmentAndLesson(enrollment, lesson);
        assertTrue(progressOpt.isPresent());
        Progress progress = progressOpt.get();
        assertTrue(progress.getCompleted());
        assertEquals(85, progress.getScore());
    }
}
