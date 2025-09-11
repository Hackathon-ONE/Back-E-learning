package learning.platform.repository;

import learning.platform.entity.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {

    //Encuentra el sumbit de un user id y el quizz id.
    Optional<QuizSubmission> findByUserIdAndQuizId(Long userId, Long quizId);
}
