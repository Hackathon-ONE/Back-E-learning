package learning.platform.service;

import learning.platform.config.TokenService;
import learning.platform.dto.AuthResponse;
import learning.platform.dto.UserLoginRequest;
import learning.platform.dto.UserRegisterRequest;
import learning.platform.dto.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import learning.platform.entity.User;


@Service
public class AuthService {

    private final UserService userService;
    private final LoginAttemptService service;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, LoginAttemptService service, TokenService tokenService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.service = service;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
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
        // 1. Validar bloqueo
        if (service.estaBloqueado(request.email())) {
            throw new RuntimeException("Tu cuenta está temporalmente bloqueada por múltiples intentos fallidos. Intenta de nuevo en 15 minutos.");
        }

        try {
            // 2. Intento de autenticación
            var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication authenticatedUser = authenticationManager.authenticate(authToken);

            // 3. Extraer User autenticado
            User user = (User) authenticatedUser.getPrincipal();

            // 4. Registrar intento exitoso
            service.registraIntentoDeLogin(request.email(), "000.000.000", true);

            // 5. Generar JWT
            String token = tokenService.generarToken(user);

            return new AuthResponse(token, user.getRole().name());

        } catch (BadCredentialsException e) {
            // 6. Registrar intento fallido
            service.registraIntentoDeLogin(request.email(), "000.000.000", false);
            throw new RuntimeException("Credenciales incorrectas");
        }
    }
}
