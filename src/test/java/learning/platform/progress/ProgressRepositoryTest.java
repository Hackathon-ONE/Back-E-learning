package learning.platform.progress;

import learning.platform.entity.Enrollment;
import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        enrollment = new Enrollment();
        enrollment.setId(1L);
        entityManager.persist(enrollment);

        lesson = new Lesson();
        lesson.setId(1L);
        entityManager.persist(lesson);

        Progress progress = new Progress();
        progress.setEnrollment(enrollment);
        progress.setLesson(lesson);
        progress.setCompleted(true);
        progress.setUpdatedAt(Instant.now());
        entityManager.persist(progress);
    }

    @Test
    void shouldFindByEnrollment() {
        List<Progress> progresses = progressRepository.findByEnrollment(enrollment);
        assertEquals(1, progresses.size());
        assertEquals(true, progresses.get(0).getCompleted());
    }

    @Test
    void shouldFindByEnrollmentAndLesson() {
        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson);
        assertNotNull(progress);
        assertEquals(true, progress.getCompleted());
    }
}