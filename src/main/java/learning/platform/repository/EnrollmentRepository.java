package learning.platform.repository;

import learning.platform.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);
    Page<Enrollment> findByStudentId(Long studentId, Pageable pageable);
    Page<Enrollment> findByCourseId(Long courseId, Pageable pageable);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

}