package learning.platform.mapper;

import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import learning.platform.entity.Course;
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

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role", qualifiedByName = "roleToString")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "profilePhoto", target = "profilePhoto")
    @Mapping(source = "about", target = "about")
    @Mapping(source = "enrolledCourses", target = "enrolledCourses", qualifiedByName = "courseListToIdList")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    @Named("mapRole")
    default Role mapRole(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    @Named("roleToString")
    default String roleToString(Role role) {
        return role.name();
    }

    @Named("courseListToIdList")
    static List<Long> courseListToIdList(List<Course> courses) {
        if (courses == null) return List.of();
        return courses.stream().map(Course::getId).toList();
    }
}