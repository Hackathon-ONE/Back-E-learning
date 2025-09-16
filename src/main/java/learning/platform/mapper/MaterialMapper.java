package learning.platform.mapper;

import learning.platform.dto.MaterialCreateRequest;
import learning.platform.dto.MaterialResponse;
import learning.platform.dto.MaterialUpdateRequest;
import learning.platform.entity.Lesson;
import learning.platform.entity.Material;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaterialMapper {

    // DTO → Entidad:
    @Mapping(target = "id", ignore = true)
    Material toEntity(MaterialCreateRequest request);

    // Entidad → DTO:
    @Mapping(source = "lesson", target = "lessonId", qualifiedByName = "mapLessonId")
    MaterialResponse toResponse(Material material);

    // Listas:
    List<MaterialResponse> toResponseList(List<Material> materials);

    @Named("mapLessonId")
    default Long mapLessonId(Lesson lesson) {
        return lesson != null ? lesson.getId() : null;
    }

    // Editar materiales:
    void updateFromDto(MaterialUpdateRequest request, @MappingTarget Material material);

}
