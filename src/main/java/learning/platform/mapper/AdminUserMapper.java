package learning.platform.mapper;

import learning.platform.dto.AdminUserDTO;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class AdminUserMapper {

    public AdminUserDTO toDto(User user) {
        AdminUserDTO dto = new AdminUserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setActive(user.isActive());
        return dto;
    }

    public void updateEntityFromDto(AdminUserDTO dto, User user) {
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        user.setActive(dto.isActive());
    }
}