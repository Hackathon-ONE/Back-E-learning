package learning.platform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import learning.platform.dto.AdminUserDTO;
import learning.platform.mapper.AdminUserMapper;
import learning.platform.entity.User;
import learning.platform.repository.UserRepository;
import learning.platform.service.AdminUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final AdminUserMapper mapper;
    public AdminUserServiceImpl(UserRepository userRepository, AdminUserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }
    @Override
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdminUserDTO updateUser(Long id, AdminUserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        mapper.updateEntityFromDto(userDTO, user);
        User updated = userRepository.save(user);
        return mapper.toDto(updated);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}