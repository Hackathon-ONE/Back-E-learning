package learning.platform.mapper;

import learning.platform.dto.ProgressUpdateRequest;
import learning.platform.dto.ProgressResponse;
import learning.platform.entity.Progress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgressMapper {

    // Solo mapea los campos simples; enrollment y lesson se setean en el servicio
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    Progress toEntity(ProgressUpdateRequest request);

    // Para la respuesta, sí necesitamos extraer los IDs
    @Mapping(source = "enrollment.id", target = "enrollmentId")
    @Mapping(source = "lesson.id", target = "lessonId")
    ProgressResponse toResponse(Progress progress);
}
