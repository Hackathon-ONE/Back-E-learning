package learning.platform.mapper;

import learning.platform.dto.LessonCreateRequest;
import learning.platform.dto.LessonResponse;
import learning.platform.entity.Course;
import learning.platform.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LessonMapper {

    // DTO → Entidad:
    @Mapping(source = "courseId", target = "course", qualifiedByName = "mapCourse")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "durationSeconds",
            expression = "java(request != null && request.durationMinutes() != null ? request.durationMinutes() * 60L : null)")
    Lesson toEntity(LessonCreateRequest request);

    // Entidad → DTO:
    @Mapping(source = "course", target = "courseId", qualifiedByName = "mapCourseId")
    @Mapping(target = "durationMinutes",
            expression = "java(lesson.getDurationSeconds() != null ? (int)(lesson.getDurationSeconds() / 60) : null)")
    LessonResponse toResponse(Lesson lesson);

    // Listas:
    List<LessonResponse> toResponseList(List<Lesson> lessons);

    // MapCourse:
    @Named("mapCourse")
    default Course mapCourse(Long courseId) {
        if (courseId == null) return null;
        Course course = new Course();
        course.setId(courseId);
        return course;
    }

    // MapCourseId:
    @Named("mapCourseId")
    default Long mapCourseId(Course course) {
        if (course == null) return null;
        return course.getId();
    }
}
