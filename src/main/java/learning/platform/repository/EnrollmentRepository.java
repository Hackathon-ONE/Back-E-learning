package learning.platform.repository;

import learning.platform.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // Podríamos añadir una búsqueda para evitar doble inscripción
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}