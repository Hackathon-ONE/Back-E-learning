package learning.platform.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import learning.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de seguridad que se ejecuta una vez por cada solicitud HTTP.
 * Se encarga de extraer y validar el token JWT, y establecer la autenticación en el contexto de seguridad.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService; // Servicio para validación y extracción de datos del token JWT
    private final UserRepository repository;

    public SecurityFilter(TokenService tokenService, UserRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    //Metodo principal del filtro, llamado para cada solicitud.


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String rawToken = authHeader.substring(7).trim();
            try {
                String subject = tokenService.getSubject(rawToken); // token puro
                repository.findByEmail(subject).ifPresent(user -> {
                    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                });
            } catch (RuntimeException ex) {
                // Token inválido -> no autenticamos; opcional: response 401 y return
                // logger.warn("Token inválido: " + ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}