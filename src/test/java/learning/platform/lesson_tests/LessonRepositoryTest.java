package learning.platform.lesson_tests;

import learning.platform.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    @Sql(scripts = "/data/lessons.sql")
    void shouldFindLessonsByCourseOrderedByOrderIndex() {
        Course course = new Course();
        course.setId(1); // Assuming course with id 1 exists
        List<Lesson> lessons = lessonRepository.findByCourseOrderByOrderIndex(course);
        assertThat(lessons).hasSize(3);
        assertThat(lessons.get(0).getOrderIndex()).isEqualTo(1);
        assertThat(lessons.get(1).getOrderIndex()).isEqualTo(2);
        assertThat(lessons.get(2).getOrderIndex()).isEqualTo(3);
    }
}
