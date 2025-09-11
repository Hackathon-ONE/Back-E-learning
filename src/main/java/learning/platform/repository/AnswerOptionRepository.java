package learning.platform.repository;

import learning.platform.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {

    /**
     * Busca todas las opciones de respuesta para una pregunta específica.
     * @param questionId El ID de la pregunta.
     * @return Una lista de AnswerOption.
     */
    List<AnswerOption> findByQuestionId(Long questionId);
}
