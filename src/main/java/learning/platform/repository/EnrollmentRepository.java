package learning.platform.repository;

import learning.platform.entity.Course;
import learning.platform.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);
    Page<Enrollment> findByStudentId(Long studentId, Pageable pageable);
    Page<Enrollment> findByCourseId(Long courseId, Pageable pageable);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    // Encuentra todos los enrollments de una lista de cursos
    List<Enrollment> findByCourseIn(List<Course> courses);

}