package learning.platform.service;

import learning.platform.dto.AdminUserDTO;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import learning.platform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminUserServiceTest {
    private UserRepository userRepository;
    private AdminUserService adminUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        adminUserService = new AdminUserService() {
            @Override
            public List<AdminUserDTO> getAllUsers() {
                return List.of();
            }

            @Override
            public AdminUserDTO updateUser(Long id, AdminUserDTO userDTO) {
                return null;
            }

            @Override
            public void deleteUser(Long id) {

            }
        };
    }

    @Test
    void shouldReturnAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setFullName("Juan Pérez");
        user1.setEmail("juan@test.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setFullName("Ana Gómez");
        user2.setEmail("ana@test.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<AdminUserDTO> users = adminUserService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Juan Pérez", users.get(0).getFullName());
        assertEquals("ana@test.com", users.get(1).getEmail());
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        Long userId = 1L;
        AdminUserDTO dto = new AdminUserDTO();
        dto.setFullName("Juan Actualizado");
        dto.setRole("INSTRUCTOR");

        User user = new User();
        user.setId(userId);
        user.setFullName("Juan Pérez");
        user.setRole(Role.valueOf("STUDENT"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AdminUserDTO updated = adminUserService.updateUser(userId, dto);

        assertEquals("Juan Actualizado", updated.getFullName());
        assertEquals("INSTRUCTOR", updated.getRole());
    }

    @Test
    void shouldDeleteUser() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> adminUserService.deleteUser(userId));
        verify(userRepository, times(1)).deleteById(userId);
    }
}