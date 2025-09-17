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

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRole")
    @Mapping(source = "password", target = "passwordHash")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRegisterRequest request);

    // ✅ Método manual para construir el DTO con cursos
    default UserResponse toResponse(User user, List<Long> enrolledCourseIds) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.isActive(),
                user.getProfilePhoto(),
                user.getAbout(),
                enrolledCourseIds
        );
    }

    List<UserResponse> toResponseList(List<User> users);

    @Named("mapRole")
    default Role mapRole(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    @Named("roleToString")
    default String roleToString(Role role) {
        return role.name();
    }
}