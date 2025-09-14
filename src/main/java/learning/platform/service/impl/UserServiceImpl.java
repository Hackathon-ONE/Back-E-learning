package learning.platform.service.impl;

import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import learning.platform.mapper.UserMapper;
import learning.platform.repository.EnrollmentRepository;
import learning.platform.repository.UserRepository;
import learning.platform.service.UserService;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           EnrollmentRepository enrollmentRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Role roleEnum;
        try {
            roleEnum = Role.valueOf(request.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + request.role());
        }

        User user = userMapper.toEntity(request);
        user.setRole(roleEnum);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setActive(true);

        if (request.urlPhoto() != null && !request.urlPhoto().isEmpty()) {
            user.setProfilePhoto(request.urlPhoto());
        }

        if (request.about() != null && !request.about().isEmpty()) {
            user.setAbout(request.about());
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser, List.of()); // ✅ lista vacía al registrar
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> userMapper.toResponse(user, List.of())); // ✅ lista vacía
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        return userMapper.toResponse(user, List.of()); // ✅ lista vacía
    }

    @Override
    public void setActive(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        user.setActive(active);
        userRepository.save(user);
    }

    @Override
    public UserResponse getCurrentUser(User user) {
        User fullUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + user.getEmail()));

        List<Long> enrolledCourseIds = enrollmentRepository.findByStudentId(fullUser.getId(), Pageable.unpaged())
                .stream()
                .map(enrollment -> enrollment.getCourse().getId())
                .toList();

        return userMapper.toResponse(fullUser, enrolledCourseIds); // ✅ cursos reales
    }
}