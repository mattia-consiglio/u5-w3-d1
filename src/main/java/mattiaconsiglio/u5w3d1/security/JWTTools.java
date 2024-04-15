package mattiaconsiglio.u5w3d1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import mattiaconsiglio.u5w3d1.entities.Employee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Employee employee) {

        long iatMs = System.currentTimeMillis();
        return Jwts.builder()
                .issuedAt(new Date(iatMs))
                .expiration(new Date(iatMs + 1000 * 60 * 60))
                .subject(employee.getId().toString())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
