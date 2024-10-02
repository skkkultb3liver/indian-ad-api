package com.dev.indianadapi.authentication.filter;

import com.dev.indianadapi.authentication.service.CustomUserDetailsService;
import com.dev.indianadapi.authentication.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException
    {

        String authHeader = request.getHeader("Authorization");
        String userEmail;
        String jwt;

        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = StringUtils.substring(authHeader, 7);
        log.info("JwtAuthFilter: JWT -- {}",  jwt);

        userEmail = jwtService.extractUsername(jwt);

        if (!userEmail.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            log.info("JwtAuthFilter: USERNAME -- {}", userDetails.getUsername());

            if (jwtService.isTokenValid(jwt, userDetails)) {

                log.info("JwtAuthFilter: Try to auth user");

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authenticationToken);

                SecurityContextHolder.setContext(context);
            }

        }

        filterChain.doFilter(request, response);
    }
}
