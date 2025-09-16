package learning.platform.mapper;

import learning.platform.dto.ProgressUpdateRequest;
import learning.platform.dto.ProgressResponse;
import learning.platform.entity.Progress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgressMapper {

    // DTO → Entidad (solo campos simples; enrollment y lesson se setean en el service):
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "completionPercentage", ignore = true) // se maneja en el service
    Progress toEntity(ProgressUpdateRequest request);

    // Entidad → DTO (extraemos IDs y completionPercentage)
    @Mapping(source = "enrollment.id", target = "enrollmentId")
    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "completionPercentage", target = "completionPercentage")
    ProgressResponse toResponse(Progress progress);
}
