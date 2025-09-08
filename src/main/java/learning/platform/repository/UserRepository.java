package learning.platform.repository;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import java.util.List;
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