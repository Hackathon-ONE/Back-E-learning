package learning.platform.service;

import learning.platform.config.TokenService;
import learning.platform.dto.AuthResponse;
import learning.platform.dto.UserLoginRequest;
import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import learning.platform.entity.User;


@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra un nuevo usuario delegando la tarea al UserService.
     * @param request Datos del usuario a registrar
     * @return El DTO de respuesta del usuario registrado
     */
    public UserResponse register(UserRegisterRequest request) {
        return userService.register(request);
    }

    /**
     * Autentica un usuario y genera un token JWT si las credenciales son válidas.
     * @param request Credenciales del usuario
     * @return DTO con el token JWT y el rol del usuario
     */
    public AuthResponse login(UserLoginRequest request) {
        // Usa AuthenticationManager para verificar credenciales de forma segura
        var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authenticatedUser = authenticationManager.authenticate(authToken);

        // Extrae el objeto User para generar el token
        User user = (User) authenticatedUser.getPrincipal();

        // Genera el token JWT
        String token = tokenService.generarToken(user);

        return new AuthResponse(token, user.getRole().name());
    }
}
