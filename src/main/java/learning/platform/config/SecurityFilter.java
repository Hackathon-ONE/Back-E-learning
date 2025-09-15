package learning.platform.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import learning.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro de seguridad que se ejecuta una vez por cada solicitud HTTP.
 * Se encarga de extraer y validar el token JWT, y establecer la autenticación en el contexto de seguridad.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Permitir explícitamente rutas públicas sin procesar token
        if (path.equals("/api/auth/login") ||
                path.equals("/api/auth/register") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Solo procesar si hay token presente
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            String subject = tokenService.getSubject(token);

            if (subject != null) {
                var usuarioOptional = repository.findByEmail(subject);
                if (usuarioOptional.isPresent()) {
                    var usuario = usuarioOptional.get();
                    String roleFromToken = tokenService.getClaimRole(token);
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleFromToken));

                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario, null, authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}