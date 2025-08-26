import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Integer> {
    List<Progress> findByEnrollment(Enrollment enrollment);
    Progress findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
}
