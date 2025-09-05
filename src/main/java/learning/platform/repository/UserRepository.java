package learning.platform.repository;
import learning.platform.entity.User;
import learning.platform.enums.Role; // Asegúrate de importar tu Enum
import java.util.List; // Import the List class
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    //Agregado por Osiris para buscar por roles
    List<User> findByRole(Role role);
}