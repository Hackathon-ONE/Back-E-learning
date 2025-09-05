package learning.platform.repository;

import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    /**
     * Todos los progresos de un alumno:
     */
    List<Progress> findByEnrollment(Enrollment enrollment);

    /**
     * Progreso de un alumno en una lección específica:
     */
    Optional<Progress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);

    /**
     * Progresos de un alumno con completionPercentage >= 100 (completados):
     */
    List<Progress> findByEnrollmentAndCompletionPercentageGreaterThanEqual(Enrollment enrollment, BigDecimal percentage);

    /**
     * Progresos de un alumno con completionPercentage < 100 (incompletos):
     */
    List<Progress> findByEnrollmentAndCompletionPercentageLessThan(Enrollment enrollment, BigDecimal percentage);
}
