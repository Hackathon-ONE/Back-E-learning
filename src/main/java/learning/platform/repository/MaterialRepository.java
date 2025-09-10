package learning.platform.repository;

import learning.platform.entity.Lesson;
import learning.platform.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Material, proporciona operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    /**
     * Encuentra todos los materiales asociados a una lección.
     *
     * @param lesson la lección a la que pertenecen los materiales
     * @return lista de materiales de esa lección
     */
    List<Material> findByLesson(@Param("lesson") Lesson lesson);
}
