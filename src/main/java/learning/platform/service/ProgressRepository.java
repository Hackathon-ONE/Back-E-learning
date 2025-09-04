package learning.platform.service;

import learning.platform.entity.Lesson;
import learning.platform.entity.Progress;
import learning.platform.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    /**
     * Encuentra todos los progresos asociados a una inscripción.
     *
     * @param enrollment Inscripción a buscar.
     * @return Lista de progresos.
     */
    List<Progress> findByEnrollment(Enrollment enrollment);

    /**
     * Encuentra el progreso asociado a una inscripción y una lección específicas.
     *
     * @param enrollment Inscripción a buscar.
     * @param lesson Lección a buscar.
     * @return Progreso encontrado, o null si no existe.
     */
    Progress findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
}