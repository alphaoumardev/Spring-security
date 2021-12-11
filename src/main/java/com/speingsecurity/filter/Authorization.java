/*
  Created by:oumar
  Project:speing-security
  Name:Authorization
  Date: 8/16/2021
  Time: 3:35 PM
*/
package com.speingsecurity.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class Authorization extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        if(request.getServletPath().equals("/api/login/**")||request.getServletPath().equals("/api/role/token"))
        {
            filterChain.doFilter(request, response);
        }
        else
        {
            String authorizationHeader=request.getHeader(AUTHORIZATION);
            if(authorizationHeader!=null&& authorizationHeader.startsWith("Bearer "))
            {
                try
                {
                    String token=authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm =Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();

                    DecodedJWT decode= verifier.verify(token);
                    String username=decode.getSubject();
                    String[] roles=decode.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority>authorities=new ArrayList<>();
                    stream(roles).forEach(role ->
                            authorities.add(new SimpleGrantedAuthority(role)));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                }
                catch(Exception ex)
                {
                    log.error("There is an error during your authorization {}",ex.getMessage());
                    response.setHeader("Hoop ERROR",ex.getMessage());
//                    response.sendError(FORBIDDEN.value());
                    response.setStatus(FORBIDDEN.value());
                    Map<String,String> error = new HashMap<>();
                    error.put("The error message",ex.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                    //to show it as json
                }
            }
            else
            {
                filterChain.doFilter(request,response);
            }
        }
    }
}
