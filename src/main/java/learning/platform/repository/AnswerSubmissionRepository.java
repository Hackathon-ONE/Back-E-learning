package learning.platform.repository;

import learning.platform.entity.AnswerSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad {@link AnswerSubmission}.
 * Proporciona métodos de acceso a datos para operaciones CRUD y consultas personalizadas
 * relacionadas con las respuestas enviadas por los usuarios.
 */
@Repository
public interface AnswerSubmissionRepository extends JpaRepository<AnswerSubmission, Long> {

    /**
     * Busca todas las respuestas de un envío de quiz específico.
     * Esta es una consulta personalizada que aprovecha las convenciones de nomenclatura
     * de Spring Data JPA para generar la consulta SQL automáticamente.
     *
     * @param quizSubmissionId El ID de la entrega del quiz.
     * @return Una lista de entidades {@link AnswerSubmission} asociadas a esa entrega.
     */
    List<AnswerSubmission> findByQuizSubmissionId(Long quizSubmissionId);
}