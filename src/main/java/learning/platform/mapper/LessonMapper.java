package learning.platform.mapper;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.dto.LessonResponse;
import learning.platform.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Duration;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LessonMapper {

    // Mapea el DTO de creación a la entidad Lesson
    @Mapping(source = "courseId", target = "course", qualifiedByName = "mapCourse")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "duration", target = "duration", qualifiedByName = "mapDuration")
    Lesson toEntity(LessonCreateRequest request);

    // Mapea la entidad Lesson al DTO de respuesta
    @Mapping(source = "course", target = "courseId", qualifiedByName = "mapCourseId")
    LessonResponse toResponse(Lesson lesson);

    // Lista de entidades a lista de DTOs:
    List<LessonResponse> toResponseList(List<Lesson> lessons);

    // Convierte Long a entidad Course:
    @Named("mapCourse")
    default Course mapCourse(Long courseId) {
        if (courseId == null) return null;
        Course course = new Course();
        course.setId(courseId);
        return course;
    }

    // Convierte Course a Long (id) para el DTO de respuesta:
    @Named("mapCourseId")
    default Long mapCourseId(Course course) {
        if (course == null) return null;
        return course.getId();
    }

    @Named("mapDuration")
    default Duration mapDuration(String duration) {
        if (duration == null || duration.isBlank()) return null; // Maneja casos nulos o vacíos.
        return Duration.parse(duration); // Convierte String (ej. "PT30M") a Duration.
    }
}
