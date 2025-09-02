package learning.platform.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import learning.platform.entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Servicio para generar y validar tokens JWT para la autenticación de usuarios.
 */

@Service
public class TokenService {

    private static final String ISSUER = "e-learning-platform";
    private final SecretConfig secretConfig;      // Configuración que contiene la clave secreta para firmar y verificar JWT

    public TokenService(SecretConfig secretConfig) {
        this.secretConfig = secretConfig;
    }

    /**
     * Genera un token JWT para el usuario proporcionado.
     * Incluye issuer, subject (email), claims personalizados (id y role) y fecha de expiración.
     * param usuario entidad Usuario con datos a incluir en el token
     * return token JWT firmado como String
     */

    public String generarToken(User usuario) {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withClaim("role", usuario.getRole().name())
                    .withExpiresAt(Instant.now().plus(72, ChronoUnit.HOURS))
                    .sign(algorithm());
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Error al crear token", ex);
        }
    }

    /**
     * Genera la fecha de expiración del token: ahora + 72 horas, zona horaria -06:00.
     * return Instant con la fecha de expiración
     */

    public static Instant generarFechaDeExpiracion(){
        return LocalDateTime.now().plusHours(72).toInstant(ZoneOffset.of("-06:00"));
    }

    /**
     * Genera el algoritmo a usar en la encripcion
     *
     */
    private Algorithm algorithm() {
        return Algorithm.HMAC256(secretConfig.getSecret());
    }

    // Verifica y devuelve DecodedJWT; lanza excepción si inválido
    private DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm())
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Token inválido", ex);
        }
    }


    /**
     * Verifica un token JWT y extrae el subject (email) si es válido.
     * param token el JWT en formato String
     * return el subject (email) contenido en el token
     */

    public String getSubject(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) throw new RuntimeException("Token nulo");
        DecodedJWT jwt = verifyToken(rawToken);
        return jwt.getSubject();
    }

    /**
     * Verifica un token JWT y extrae el claim 'id' (ID de usuario) si es válido.
     * param token el JWT en formato String
     * return el claim 'id' como Long
     */

    public Long getClaimId(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) throw new RuntimeException("Token nulo");
        DecodedJWT jwt = verifyToken(rawToken);
        if (jwt.getClaim("id").isNull()) throw new RuntimeException("Claim id no encontrado");
        return jwt.getClaim("id").asLong();
    }

    //Lo mismo pero para rol

    public String getClaimRole(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) throw new RuntimeException("Token nulo");
        DecodedJWT jwt = verifyToken(rawToken);
        if (jwt.getClaim("role").isNull()) throw new RuntimeException("Claim role no encontrado");
        return jwt.getClaim("role").asString();
    }
}
