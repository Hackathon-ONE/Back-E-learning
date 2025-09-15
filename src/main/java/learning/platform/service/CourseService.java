package learning.platform.service;

import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.entity.Course;
import learning.platform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CourseService {
    //Crear curso
    CourseResponseDTO createCourse(CourseRequestDTO dto, User instructor);
    //Actualizar curso
    CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO dto);
    //Borrar curso
    void deleteCourse(Long courseId);
    //Mostrar todos los cursos
    Page<CourseResponseDTO> findAllPublicCourses(Pageable pageable);
    //Mostrar curso por Id
    CourseResponseDTO findCourseDtoById(Long id);
    //enrollar al estudiante en un curso, pero lo quitamos
    //void enrollStudentInCourse(Long courseId, User student);
    // Buscar un curso por su ID y devolver la entidad
    Course findCourseById(Long id);
    // Método en la interfaz CourseService
    CourseResponseDTO publishCourse(Long courseId, boolean published);
}