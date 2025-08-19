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
    @Autowired
    private TokenService tokeService; // Servicio para validación y extracción de datos del token JWT
    @Autowired
    private UserRepository repository;

    //Metodo principal del filtro, llamado para cada solicitud.


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtiene la cabecera 'Authorization' de la solicitud
        var authHeader = request.getHeader("Authorization");
        // Verifica que la cabecera exista y no esté vacía
        if (authHeader != null && !authHeader.isEmpty()
                && authHeader.startsWith("Bearer ")) {
            // Elimina el prefijo 'Bearer ' para obtener el token puro
            var token = authHeader.replace("Bearer ", "");
            // Extrae el subject (email) del token
            var subject = tokeService.getSubject(token);
            // verifica si se pudo extraer un subject válido
            if (subject != null) {
                var usuario = repository.findByEmail(subject);
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        // Continúa con la siguiente etapa de la cadena de filtros
        filterChain.doFilter(request,response);
    }
}
