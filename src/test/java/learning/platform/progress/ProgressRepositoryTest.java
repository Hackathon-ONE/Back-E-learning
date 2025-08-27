package learning.platform.progress;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProgressRepositoryTest {

    @Autowired
    private ProgressRepository progressRepository;

    @Test
    @Sql(scripts = "/data/progress.sql")
    void shouldFindByEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1); // Assuming enrollment with id 1 exists
        List<Progress> progresses = progressRepository.findByEnrollment(enrollment);
        assertThat(progresses).hasSize(2);
    }

    @Test
    @Sql(scripts = "/data/progress.sql")
    void shouldFindByEnrollmentAndLesson() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1);
        Lesson lesson = new Lesson();
        lesson.setId(1);
        Progress progress = progressRepository.findByEnrollmentAndLesson(enrollment, lesson);
        assertThat(progress).isNotNull();
        assertThat(progress.getEnrollment().getId()).isEqualTo(1);
        assertThat(progress.getLesson().getId()).isEqualTo(1);
    }
}
