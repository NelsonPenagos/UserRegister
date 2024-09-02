package com.company.register.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Genera una clave secreta para firmar los JWTs
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tiempo de expiración del token (por ejemplo, 1 hora)
    private static final long EXPIRATION_TIME = 3600000;

    /**
     * Genera un JWT basado en el nombre de usuario.
     *
     * @param username el nombre de usuario para el cual generar el token
     * @return el token JWT firmado
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    /**
     * Valida el token JWT.
     *
     * @param token el token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     *
     * @param token el token JWT del cual extraer la información
     * @return el nombre de usuario contenido en el token
     */
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     *
     * @param token el token JWT del cual extraer la información
     * @return la fecha de expiración del token
     */
    public Date extractExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    /**
     * Verifica si el token ha expirado.
     *
     * @param token el token JWT a verificar
     * @return true si el token ha expirado, false en caso contrario
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
