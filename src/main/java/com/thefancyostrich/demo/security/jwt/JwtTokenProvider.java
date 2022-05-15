package com.thefancyostrich.demo.security.jwt;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.thefancyostrich.demo.users.UserService;
import com.thefancyostrich.demo.users.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    // Should be configured in a secure manner
    private String securityKey = "SecrEt";

    private long validityInMilliseconds = 10800000; // 3h

    private UserService userService;

    /**
     * Create a new token for an authenticated user.
     * 
     * @param username
     * @param role
     * @return
     */
    public String createToken(String username, UserRole role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", role.getGrantedAuthoritiesAsStrings());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, securityKey)
                .compact();
    }

    /**
     * Return authentication from token.
     * 
     * @param token
     * @return The authentication
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Extract the username/subject from a token.
     * 
     * @param token The token
     * @return The username
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Extract the token from the http request.
     * If invalid authorization header null is returned.
     * 
     * @param req Http request
     * @return Token
     */
    public String extractToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.split(" ")[0];
        }
        return null;
    }

    /**
     * Validate token. Returns true if the token is valid otherwise an exception is
     * thrown.
     * 
     * @param token
     * @return
     */

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalStateException("Invalid JWT token.");
        }
    }

}
