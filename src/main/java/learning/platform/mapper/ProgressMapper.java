import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProgressMapper {

    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    Progress toEntity(ProgressUpdateRequest request);

    @Mapping(source = "enrollment.id", target = "enrollmentId")
    @Mapping(source = "lesson.id", target = "lessonId")
    ProgressResponse toResponse(Progress progress);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget Progress progress, ProgressUpdateRequest request);
}
