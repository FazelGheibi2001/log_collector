package com.example.log_collect.security;

import com.example.log_collect.dtos.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDTO usernamePasswordDTO = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginDTO.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    usernamePasswordDTO.getUsername(),
                    usernamePasswordDTO.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String role = "";
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        while (authorities.iterator().hasNext()) {
            String value = authorities.iterator().next().getAuthority();
            if (value.contains("Role: ")) {
                role = value.replace("Role: ", "");
                break;
            }
        }

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("role", role)
                .claim("authorities", authorities)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(5 * 60)))
                .signWith(Keys.hmacShaKeyFor(SecureKey.secureKey.getBytes()))
                .compact();
        response.addHeader("Authorization", token);
    }
}
