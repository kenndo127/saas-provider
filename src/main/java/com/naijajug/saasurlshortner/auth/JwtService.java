package com.naijajug.saasurlshortner.auth;

import com.naijajug.saasurlshortner.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtService {
    @Value("${saasurl.jwt.secret}")
    public String secret;

    public String generateToken(String email, Set<Roles> userRoles, String activeSessionId, UUID userId){ //use email as username
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userRoles.stream().map(Enum::name).collect(Collectors.toSet()));
        claims.put("sessionId",activeSessionId);
        claims.put("tenancyId",String.valueOf(userId));
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserSessionId(String token) {
        Claims claims = extractAllClaims(token);
        return  (String)claims.get("sessionId");
    }
    public String extractTenancyId(String token) {
        Claims claims = extractAllClaims(token);
        return  (String)claims.get("tenancyId");
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Set<Roles> extractRoles(String token){
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("role",List.class);
        return roles.stream().map(Roles::valueOf).collect(Collectors.toSet());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
