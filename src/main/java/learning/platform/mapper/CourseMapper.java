package learning.platform.mapper;

import learning.platform.dto.CourseRequestDTO;
import learning.platform.dto.CourseResponseDTO;
import learning.platform.entity.Course;
import org.springframework.stereotype.Component;

@Component // Lo marcamos como un componente de Spring para poder inyectarlo
public class CourseMapper {

    // Convierte de DTO de petición a Entidad (para crear cursos)
    public Course toEntity(CourseRequestDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setCategory(dto.getCategory());
        course.setPublished(dto.getPublished() != null && dto.getPublished());

        if (dto.getUrlPhoto() != null && !dto.getUrlPhoto().isEmpty()){
            course.setProfilePhoto(dto.getUrlPhoto());
        }

        if (dto.getAbout() != null && !dto.getAbout().isEmpty()){
            course.setAbout(dto.getAbout());
        }


        return course;
    }

    // Convierte de Entidad a DTO de respuesta (para enviar al cliente)
    public CourseResponseDTO toResponseDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setSlug(course.getSlug());
        if (course.getInstructor() != null) {
            dto.setInstructorName(course.getInstructor().getFullName());
        }
        if (course.getProfilePhoto() != null && !course.getProfilePhoto().isEmpty()){
            dto.setUrlPhoto(course.getProfilePhoto());
        }

        if (course.getAbout() != null && !course.getAbout().isEmpty()){
            dto.setAbout(course.getAbout());
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
        if (dto.getTitle() != null) {
            course.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
        }
        if (dto.getCategory() != null) {
            course.setCategory(dto.getCategory());
        }
        if (dto.getPublished() != null) {
            course.setPublished(dto.getPublished());
        }
    }
}