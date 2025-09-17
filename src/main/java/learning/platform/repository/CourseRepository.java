package learning.platform.repository;

import learning.platform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

// JpaSpecificationExecutor nos permite crear queries dinámicas para los filtros
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    // Busca todos los cursos de un instructor por su id
    List<Course> findByInstructorId(Long instructorId);
    // Verifica si existe al menos un curso para un instructor
    boolean existsByInstructor_Id(Long instructorId);
}