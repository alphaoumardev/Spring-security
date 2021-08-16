/*
  Created by:oumar
  Project:speing-security
  Name:UserRepo
  Date: 8/15/2021
  Time: 5:17 PM
*/
package com.speingsecurity.repository;

import com.speingsecurity.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users,Long>
{
   Users findByUsername(String username);
}
