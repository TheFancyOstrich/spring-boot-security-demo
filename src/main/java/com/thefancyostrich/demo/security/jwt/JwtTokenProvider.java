package com.thefancyostrich.demo.security.jwt;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.thefancyostrich.demo.security.UserFinderService;
import com.thefancyostrich.demo.users.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.secret}")
    private String securityKey;

    @Value("${jwt.validityTime}")
    private long validityInMilliseconds;

    private final UserFinderService userFinderService;

    @Autowired
    public JwtTokenProvider(UserFinderService userFinderService) {
        this.userFinderService = userFinderService;
    }

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
        UserDetails userDetails = userFinderService.loadUserByUsername(getUsername(token));

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
            return bearerToken.split(" ")[1];
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
