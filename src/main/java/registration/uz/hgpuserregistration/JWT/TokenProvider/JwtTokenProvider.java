package registration.uz.hgpuserregistration.JWT.TokenProvider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
//    private static final long JWT_EXPIRATION_TIME = 86400000;
//    private final Key key;
//    private final JwtParser jwtParser;
//
//    public JwtTokenProvider() {
//        byte[] keyBytes;
//        String secret = "Ckl0IHNlZW1zIHlvdSdyZSB0cnlpbmcgdG8gaW1wbGVtZW50IGEgbG9naW4gbWV0aG9kIGluIGEgU3ByaW5nIEJvb3QgYXBwbGljYXRpb24uIFRoZSBlcnJvciBtaWdodCBiZSBiZWNhdXNlIHlvdSBoYXZlbid0IGhhbmRsZWQgdGhlIGNhc2Ugd2hlbiBhdXRoZW50aWNhdGlvbiBmYWlscy4=";
//        keyBytes = Base64.getDecoder().decode(secret);
//        key = Keys.hmacShaKeyFor(keyBytes);
//        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
//    }
//
//    public String createToken(Authentication authentication) {
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
//
//        long now = System.currentTimeMillis();
//        Date jwtExpiration = new Date(now + JWT_EXPIRATION_TIME);
//
//        return Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim("auth", authorities)
//                .signWith(key, SignatureAlgorithm.ES512)
//                .setExpiration(jwtExpiration)
//                .compact();
//    }
private final JwtParser jwtParser;

    private final long milliseconds;

    private final Key key;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
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
            jwtParser.parseClaimsJwt(token);
        }catch (ExpiredJwtException e){
            log.error("Expired JWT");
        }catch (UnsupportedJwtException e){
            log.error("Unsupported JWT");
        }catch (MalformedJwtException e){
            log.error("Malformed JWT");
        }catch (IllegalArgumentException e){
            log.error("Illegal JWT");
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
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
