/*
  Created by:oumar
  Project:speing-security
  Name:UserImp
  Date: 8/15/2021
  Time: 5:30 PM
*/
package com.speingsecurity.services;

import com.speingsecurity.models.Role;
import com.speingsecurity.models.Users;
import com.speingsecurity.repository.RoleRepo;
import com.speingsecurity.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserImp implements UserService, UserDetailsService
{
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    //This is the userdetailService method
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if(username==null)
        {
            log.error("The username is not found in the database [log line]");
            throw new UsernameNotFoundException("The username is not founud in the database");
        }
        else
        {
            log.error("The username found in the database [log line] {}",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRole().forEach(role->
                authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new User(user.getUsername(),user.getPassword(),authorities);
//        import org.springframework.security.core.userdetails.[User]; this user
    }
    @Override
    public Users saveUser(Users user)
    {
        log.info("saving the new user {} to the database",user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role)
    {
        log.info("saving the role to the database");
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName)
    {
        log.info("The username is {} and the rolename is {} ", username,roleName);
        Users user =userRepo.findByUsername(username);
        Role role =roleRepo.findByName(roleName);
        user.getRole().add(role);
    }

    @Override
    public Users getUser(String username)
    {
        log.info("The username {}",username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<Users> getUsers()
    {
        log.info("fitching all users");
        return userRepo.findAll();
    }

}
