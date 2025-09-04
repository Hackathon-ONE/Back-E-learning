package learning.platform.repository;

import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    List<Progress> findByEnrollment(Enrollment enrollment);

    Optional<Progress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
}
