package com.gdbjr.idealit.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

    static final Long EXPIRATION_TIME = 999_999_999L;
    static final String SECRET = "@IdealitWjtSecret2018@";
    static final String TOKEN_PREFIX = "Idealit";
    static final String HEADER_STRING = "Authorization";

    static void addAuthentication(HttpServletResponse response, String username) throws IOException {

        String token = keyGen(username);
        response.addHeader(HEADER_STRING, token);

        // RETURN JSON WITH TOKEN
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"token\": \"" + token + "\"}");
        out.flush();
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            try {
                // faz parse do token
                String user = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody()
                        .getSubject();

                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                }
            } catch (MalformedJwtException e){
                System.out.println("INVALID TOKEN");
            }

        }
        return null;
    }

    public static String keyGen(String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return TOKEN_PREFIX + " " + JWT;
    }
}
