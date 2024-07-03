package com.sideproject.mercatus.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.sideproject.mercatus.model.LocalUser;
import com.sideproject.mercatus.model.dao.LocalUserDAO;
import com.sideproject.mercatus.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    JWTService jwtService;

    @Autowired
    LocalUserDAO dao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            try {
                String username = jwtService.getUsername(token);
                Optional<LocalUser> user = dao.findByUsernameIgnoreCase(username);
                if (user.isPresent()) {
                    LocalUser localUser = user.get();
                    UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(localUser, null, new ArrayList<>());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTDecodeException exception) {
            }
        }
        filterChain.doFilter(request, response);
    }
}
