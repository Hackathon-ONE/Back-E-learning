package learning.platform.mapper;

import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    // Mapea el DTO de registro a la entidad User
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRole")
    @Mapping(source = "password", target = "passwordHash")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRegisterRequest request);

    // Mapea la entidad User al DTO de respuesta
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "roles", expression = "java(List.of(user.getRole().name()))")
    @Mapping(target = "enrolledCourses", expression = "java(user.getEnrolledCourseIds())")
    UserResponse toResponse(User user);

    // Mapea una lista de entidades User a una lista de respuestas
    List<UserResponse> toResponseList(List<User> users);

    // Convierte String a Enum Role
    @Named("mapRole")
    default Role mapRole(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}