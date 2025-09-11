package learning.platform.repository;

import learning.platform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    //Encuentra los quizzes asociados con un id de algún curso.
    List<Quiz> findByCourseId(Long courseId);

    // Encuentra quizz asociado a alguna lección.
    Optional<Quiz> findByLessonId(Long lessonId);
}
