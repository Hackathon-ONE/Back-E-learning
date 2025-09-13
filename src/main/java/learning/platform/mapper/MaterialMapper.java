package learning.platform.mapper;

import learning.platform.dto.MaterialCreateRequest;
import learning.platform.dto.MaterialResponse;
import learning.platform.entity.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaterialMapper {

    // DTO → Entidad:
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lesson", ignore = true) // se setea en el service
    Material toEntity(MaterialCreateRequest request);

    // Entidad → DTO:
    MaterialResponse toResponse(Material material);

    // Listas:
    List<MaterialResponse> toResponseList(List<Material> materials);
}
