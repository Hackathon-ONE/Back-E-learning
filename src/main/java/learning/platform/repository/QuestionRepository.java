package learning.platform.repository;

import learning.platform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Encuentra todas las preguntas con un ID específico.
    List<Question> findByQuizId(Long quizId);
}
