package learning.platform.service.impl;

import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import learning.platform.entity.Course;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import learning.platform.mapper.UserMapper;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.UserRepository;
import learning.platform.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           CourseRepository courseRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
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
    public void setActive(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        user.setActive(active);
        userRepository.save(user);
    }

    @Override
    public UserResponse mapToUserResponse(User user) {
        List<String> roles = List.of(user.getRole().name());
        List<Long> enrolledCourses = user.getEnrolledCourseIds();

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                roles,
                enrolledCourses
        );
    }

    // ✅ Nuevo método para evitar LazyInitializationException
    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmailWithCourses(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + email));
        return mapToUserResponse(user);
    }

    // (Opcional) Método para inscribir cursos si lo necesitás
    @Override
    public void enrollUserInCourses(Long userId, List<Long> courseIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + userId));

        List<Course> courses = courseRepository.findAllById(courseIds);
        user.setEnrolledCourses(courses);
        userRepository.save(user);
    }
}