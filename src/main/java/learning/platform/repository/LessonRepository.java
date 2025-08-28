package learning.platform.repository;

import learning.platform.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Lesson, proporciona operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    /**
     * Encuentra todas las lecciones asociadas a un curso, ordenadas por orderIndex.
     *
     * @param course el curso al que pertenecen las lecciones
     * @return lista de lecciones ordenadas por orderIndex
     */
    List<Lesson> findByCourseOrderByOrderIndex(@Param("course") Course course);
}