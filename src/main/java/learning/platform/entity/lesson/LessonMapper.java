import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "course", ignore = true)
    Lesson toEntity(LessonCreateRequest request);

    @Mapping(source = "course.id", target = "courseId")
    LessonResponse toResponse(Lesson lesson);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    void updateEntityFromRequest(@MappingTarget Lesson lesson, LessonCreateRequest request);
}
