package learning.platform.mapper;

import learning.platform.dto.lesson.LessonCreateRequest;
import learning.platform.dto.lesson.LessonResponse;
import learning.platform.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LessonMapper {

    // Mapea el DTO de creación a la entidad Lesson:
    @Mapping(source = "courseId", target = "course", qualifiedByName = "mapCourse")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "contentUrl", target = "contentUrl")
    @Mapping(source = "contentType", target = "contentType")
    @Mapping(source = "orderIndex", target = "orderIndex")
    @Mapping(source = "duration", target = "duration")
    @Mapping(target = "id", ignore = true)
    Lesson toEntity(LessonCreateRequest request);

    // Mapea la entidad Lesson al DTO de respuesta:
    @Mapping(source = "id", target = "id")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "contentUrl", target = "contentUrl")
    @Mapping(source = "contentType", target = "contentType")
    @Mapping(source = "orderIndex", target = "orderIndex")
    @Mapping(source = "duration", target = "duration")
    LessonResponse toResponse(Lesson lesson);

    // Mapea una lista de entidades Lesson a una lista de respuestas:
    List<LessonResponse> toResponseList(List<Lesson> lessons);

    // Convierte Long a entidad Course:
    @Named("mapCourse")
    default Course mapCourse(Long courseId) {
        if (courseId == null) {
            return null;
        }
        Course course = new Course();
        course.setId(courseId);
        return course;
    }
}