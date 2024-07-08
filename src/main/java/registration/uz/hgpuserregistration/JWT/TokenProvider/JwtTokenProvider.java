package registration.uz.hgpuserregistration.JWT.TokenProvider;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
private final JwtParser jwtParser;

private final UserDetailsService userDetailsService;

    private final long milliseconds;

    private final Key key;

    public JwtTokenProvider(UserDetailsService userDetailsService, UserDetailsService userDetailsService1) {
        this.userDetailsService = userDetailsService1;
        byte[] keyBytes;
        String secret = "Ckl0IHNlZW1zIHlvdSdyZSB0cnlpbmcgdG8gaW1wbGVtZW50IGEgbG9naW4gbWV0aG9kIGluIGEgU3ByaW5nIEJvb3QgYXBwbGljYXRpb24uIFRoZSBlcnJvciBtaWdodCBiZSBiZWNhdXNlIHlvdSBoYXZlbid0IGhhbmRsZWQgdGhlIGNhc2Ugd2hlbiBhdXRoZW50aWNhdGlvbiBmYWlscy4";
        keyBytes = Base64.getDecoder().decode(secret);
        key = new SecretKeySpec(keyBytes, "HmacSHA512");
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        milliseconds = 864000000;
    }

    public String createToken(Authentication authentication) {
        try {
            String role = authentication.getAuthorities().stream()
                    .map(roles -> new SimpleGrantedAuthority(roles.getAuthority()))
                    .map(SimpleGrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            long now = System.currentTimeMillis();

            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .claim("role", role)
                    .setExpiration(new Date(now + milliseconds))
                    .signWith(key, SignatureAlgorithm.HS512) // Use HS512 instead of ES512
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error creating JWT token", e);
        }
    }


    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true; // Token is valid
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Illegal JWT: {}", e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("role").toString().split(","))
                .filter(auth -> auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .toList();
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
