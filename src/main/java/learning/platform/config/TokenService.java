package learning.platform.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import learning.platform.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Servicio para generar y validar tokens JWT para la autenticación de usuarios.
 */

@Service
public class TokenService {
    @Autowired
    private SecretConfig secretConfig;      // Configuración que contiene la clave secreta para firmar y verificar JWT

    /**
     * Genera un token JWT para el usuario proporcionado.
     * Incluye issuer, subject (email), claims personalizados (id y role) y fecha de expiración.
     * param usuario entidad Usuario con datos a incluir en el token
     * return token JWT firmado como String
     */

    public String generarToken(User usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretConfig.getSecret());
            return JWT.create()
                    .withIssuer("elearning") // Emisor del token
                    .withSubject(usuario.getEmail())    // Subject: email del usuario
                    .withClaim("id", usuario.getId())   // Claim adicional: id de usuario
                    .withClaim("role", usuario.getRole().name()) // Claim adicional: rol del usuario
                    .withExpiresAt(generarFechaDeExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            // Si ocurre un error al crear el token, lanza una excepción genérica
            throw new RuntimeException();
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
     * Verifica un token JWT y extrae el subject (email) si es válido.
     * param token el JWT en formato String
     * return el subject (email) contenido en el token
     */

    public String getSubject(String token) {
        if (token == null){
            throw new RuntimeException("Token nullo");
        }
        DecodedJWT verifier =null;
        // Configura el verificador con el mismo algoritmo y emisor
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretConfig.getSecret());
            verifier = JWT.require(algorithm)
                    .withIssuer("elearning")
                    .build()
                    .verify(token); // Verifica la firma y validez
        } catch (JWTVerificationException exception){
            // Loguea información de la excepción y continúa
            System.out.println(exception.toString());
        }
        if (verifier.getSubject() == null){
            throw new RuntimeException("Subject invalido");
        }
        return verifier.getSubject();
    }

    /**
     * Verifica un token JWT y extrae el claim 'id' (ID de usuario) si es válido.
     * param token el JWT en formato String
     * return el claim 'id' como Long
     */

    public Long getClaimId(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("No se encontró Bearer token en la cabecera");
        }
        token = token.substring(7).trim();
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretConfig.getSecret());
            verifier = JWT.require(algorithm)
                    .withIssuer("elearning")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            System.out.println(exception.toString());
        }
        if (verifier.getClaim("id") == null) {
            throw new RuntimeException("Subject invalido");
        }
        return verifier.getClaim("id").asLong();
    }

    //Lo mismo pero para rol

    public String getClaimRole(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("No se encontró Bearer token en la cabecera");
        }
        token = token.substring(7).trim();
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretConfig.getSecret());
            verifier = JWT.require(algorithm)
                    .withIssuer("elearning")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            System.out.println(exception.toString());
        }
        if (verifier.getClaim("role") == null) {
            throw new RuntimeException("Subject invalido");
        }
        return verifier.getClaim("role").asString();
    }
}
