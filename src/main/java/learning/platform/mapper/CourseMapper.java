package learning.platform.mapper;

import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    // Convierte de DTO de petición a Entidad (para crear cursos)
    public Course toEntity(CourseRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setCategory(dto.getCategory());
        course.setUrlPhoto(dto.getUrlPhoto());
        course.setAbout(dto.getAbout());

        return course;
    }

    // Convierte de Entidad a DTO de respuesta (para enviar al cliente)
    public CourseResponseDTO toResponseDTO(Course course) {
        if (course == null) {
            return null;
        }

        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setSlug(course.getSlug());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setCategory(course.getCategory());
        dto.setUrlPhoto(course.getUrlPhoto()); // Se corrige el mapeo
        dto.setAbout(course.getAbout());

        // Manejo del instructor para evitar NullPointerException si es nulo
        if (course.getInstructor() != null) {
            dto.setInstructorName(course.getInstructor().getFullName());
        }
        return dto;
    }

    /**
     * Actualiza una Entidad Course existente a partir de un DTO de petición.
     */
    public void updateCourseFromDTO(CourseRequestDTO dto, Course course) {
        if (dto == null || course == null) {
            return;
        }
        // Se actualizan los campos solo si no son nulos en el DTO
        if (dto.getTitle() != null) {
            course.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
        }
        if (dto.getCategory() != null) {
            course.setCategory(dto.getCategory());
        }
        if (dto.getUrlPhoto() != null) {
            course.setUrlPhoto(dto.getUrlPhoto());
        }
        if (dto.getAbout() != null) {
            course.setAbout(dto.getAbout());
        }
    }
}