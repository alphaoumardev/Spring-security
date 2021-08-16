/*
  Created by:oumar
  Project:speing-security
  Name:RoleRepo
  Date: 8/15/2021
  Time: 5:21 PM
*/
package com.speingsecurity.repository;

import com.speingsecurity.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long>
{
    Role findByName(String name);
}
