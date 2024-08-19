package com.example.CONFIG;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    //Crear Token de acceso
    public String generedAccesToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignaturekey(), SignatureAlgorithm.HS256)
                .compact();

    }

    //Validar token de acceso
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignaturekey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            log.error("Token invalido, error: ".concat(e.getMessage()));
            return false;
        }
    }

    //Obtener todos los clains
    public Claims extractAllsClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignaturekey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //obtener Username del token
    public String getUsernameFromToken(String Token) {
        return getClaims(Token, Claims::getSubject);
    }


    //obtner solo uno calim
    public <T> T getClaims(String Token, Function<Claims, T> claimsTFunction) {

        Claims claims = extractAllsClaims(Token);
        return claimsTFunction.apply(claims);

    }


    //Obtener firma token
    public Key getSignaturekey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
