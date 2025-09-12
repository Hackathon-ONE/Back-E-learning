package learning.platform.service.impl;

import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import learning.platform.helper.AuditContext;
import learning.platform.helper.AuditPropagator;
import learning.platform.mapper.UserMapper;
import learning.platform.repository.UserRepository;
import learning.platform.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuditContext auditContext;
    private final AuditPropagator auditPropagator;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, AuditContext auditContext, AuditPropagator auditPropagator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.auditContext = auditContext;
        this.auditPropagator = auditPropagator;
    }


    @Override
    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validación defensiva del rol (ya validado por @Pattern en el DTO)
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(request.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + request.role());
        }

        // Mapeo del DTO a entidad
        User user = userMapper.toEntity(request);
        user.setRole(roleEnum); // Asignación explícita del enum
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setActive(true);

        if (request.urlPhoto() != null && !request.urlPhoto().isEmpty()){
            user.setProfilePhoto(request.urlPhoto());
        }

        if (request.about() != null && !request.about().isEmpty()){
            user.setAbout(request.about());
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toResponse);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void setActive(Long id, boolean active, User userAuth) {
        auditContext.setCurrentUser("user:" + userAuth.getId());
        auditContext.setSessionId("session-" + System.currentTimeMillis());

        auditPropagator.propagate();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        user.setActive(active);
        userRepository.save(user);
    }
}