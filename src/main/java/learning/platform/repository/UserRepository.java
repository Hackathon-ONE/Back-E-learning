package learning.platform.repository;

import learning.platform.entity.User;
import learning.platform.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    /**
     * Carga el usuario junto con los cursos inscriptos para evitar LazyInitializationException.
     * Se usa en el endpoint /api/users/me para devolver el perfil completo.
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.enrolledCourses WHERE u.email = :email")
    Optional<User> findByEmailWithCourses(@Param("email") String email);
}