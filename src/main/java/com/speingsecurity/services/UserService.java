/*
  Created by:oumar
  Project:speing-security
  Name:UserService
  Date: 8/15/2021
  Time: 5:23 PM
*/
package com.speingsecurity.services;

import com.speingsecurity.models.Role;
import com.speingsecurity.models.Users;

import java.util.List;

public interface UserService
{
    Users saveUser(Users user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);//to add the username
    Users getUser(String username);
    List<Users> getUsers();
}
