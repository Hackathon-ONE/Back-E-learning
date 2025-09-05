package learning.platform.service;

import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;

import java.util.Optional;

public interface UserService {

    UserResponse register(UserRegisterRequest request);

    Optional<UserResponse> findByEmail(String email);

    UserResponse getById(Long id);

    void setActive(Long id, boolean active);

}

