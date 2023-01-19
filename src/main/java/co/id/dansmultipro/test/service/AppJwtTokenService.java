package co.id.dansmultipro.test.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class AppJwtTokenService {

    private String tokenKey = "46294A404E635266556A586E3272357538782F413F4428472B4B615064536756";

    public Boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return isTokenNonExpired(token);
        } catch (SignatureException e) {
            log.debug("signature exception" + e);
        } catch (MalformedJwtException e) {
            log.debug("token malformed" + e);
        } catch (ExpiredJwtException e) {
            log.debug("token expired" + e);
        } catch (UnsupportedJwtException e) {
            log.debug("unsupported" + e);
        } catch (IllegalArgumentException e) {
            log.debug("Illegal" + e);
        }

        return false;
    }

    public Boolean isTokenValid(String token, UserDetails appUserDetail) {
        return isTokenNonExpired(token) && isUsernameRegistered(token, appUserDetail);
    }

    public String extractUsernameFromToken(String token) {
        String username = extractAllClaims(token, Claims::getSubject);

        return username;
    }

    private <T> T extractAllClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractClaims(token);

        return claimResolver.apply(claims);
    }

    public String generateAccessToken(String username, UserDetails userDetails) {
        Map<String, Object> authority = new HashMap<>();
        authority.put("authority", userDetails.getAuthorities());

        return Jwts
                .builder()
                .setClaims(authority)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(createKey(), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .compact();
    }

    private Claims extractClaims(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(createKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    private Boolean isUsernameRegistered(String token, UserDetails userDetails) {
        String username = extractUsernameFromToken(token);

        return userDetails.getUsername().equals(username);
    }

    public Boolean isTokenNonExpired(String token) {
        Date tokenDate = extractAllClaims(token, Claims::getExpiration);
        return tokenDate.after(new Date());
    }

    private Key createKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

}
