package learning.platform.service;

import learning.platform.dto.AdminUserDTO;
import java.util.List;

public interface AdminUserService {
    List<AdminUserDTO> getAllUsers();
    AdminUserDTO updateUser(Long id, AdminUserDTO userDTO);
    void deleteUser(Long id);
}
