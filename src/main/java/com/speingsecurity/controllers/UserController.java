/*
  Created by:oumar
  Project:speing-security
  Name:UserController
  Date: 8/16/2021
  Time: 10:06 AM
*/
package com.speingsecurity.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speingsecurity.models.Role;
import com.speingsecurity.models.Users;
import com.speingsecurity.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class UserController
{
    private final UserService userService;

    @GetMapping(path="/users")
    public ResponseEntity<List<Users>> getUsers()
    {
        return ResponseEntity.ok().body(userService.getUsers());
    }
    @PostMapping(path="/user/save")
    public ResponseEntity<Users> saveUsers(@RequestBody Users user)
    {
//        this is to create the url path
        URI uri =URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }
    @PostMapping(path="/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role)
    {
        URI uri =URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }
//    create a class
    @Data
    static
    class RoleUserForm
    {
        private String username;
        private String roleName;
    }
    @PostMapping(path="/role/adduser")
    public ResponseEntity<?> addUserRole(@RequestBody RoleUserForm form )
    {
        userService.addRoleToUser(form.getRoleName(),form.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping(path="/role/token")
    public void addtoken(HttpServletRequest request, HttpServletResponse response)
    {
        String authorizationHeader=request.getHeader(AUTHORIZATION);

        if(authorizationHeader!=null&& authorizationHeader.startsWith("Bearer "))
        {
            try {
                String token = authorizationHeader.substring("Bearer ".length());

                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();

                DecodedJWT decode = verifier.verify(token);
                String username = decode.getSubject();
                Users user = userService.getUser(username);//get the username from the database
                String acces_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("Roles",user.getRole()
                                .stream().map(Role::getName)
                                .collect(Collectors.toList()))
                        .sign(algorithm);

//                response.addHeader("Authorization",token);
//                response.addHeader("The refresh token",refreshToken);

                Map<String,String> tokens = new HashMap<>();
                tokens.put("Authorization",acces_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
                //to show it as json

                String[] roles = decode.getClaim("roles").asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            }

            catch(Exception ex)
            {
                log.error("There is an error during your authorization {}",ex.getMessage());
                response.setHeader("Hoop ERROR",ex.getMessage());
//                    response.sendError(FORBIDDEN.value());
                response.setStatus(FORBIDDEN.value());

            }
        }
        else
        {
            throw new RuntimeException("The refresh token is missing ");
        }
    }
}
