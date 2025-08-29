package learning.platform.mapper;

import learning.platform.dto.ProgressUpdateRequest;
import learning.platform.dto.ProgressResponse;
import learning.platform.entity.Progress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgressMapper {

    @Mapping(source = "enrollmentId", target = "enrollment.id")
    @Mapping(source = "lessonId", target = "lesson.id")
    @Mapping(source = "completed", target = "completed")
    @Mapping(source = "score", target = "score")
    @Mapping(source = "updatedAt", target = "updatedAt")
    Progress toEntity(ProgressUpdateRequest request); // Enrollment y Lesson se inyectan en el servicio

    @Mapping(source = "enrollment.id", target = "enrollmentId")
    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "completed", target = "completed")
    @Mapping(source = "score", target = "score")
    @Mapping(source = "updatedAt", target = "updatedAt")
    ProgressResponse toResponse(Progress progress);
}