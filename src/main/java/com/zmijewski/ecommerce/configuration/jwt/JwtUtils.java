package com.zmijewski.ecommerce.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {
    private String secret;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenExpiration);
    }
    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenExpiration);
    }
    public String getUsernameFromToken(String token) {
        return getDataFromToken(token).getSubject();
    }
    public Date getExpirationDateFromToken(String token) {
        return getDataFromToken(token).getExpiration();
    }

    public Boolean isTokenValid(String token, String user) {
        final String username = getUsernameFromToken(token);
        return username.equals(user) && !isTokenExpired(token);
    }


    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Claims getDataFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private String generateToken(String userName, Long accessTime) {
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTime * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }



}
