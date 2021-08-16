/*
  Created by:oumar
  Project:speing-security
  Name:Users
  Date: 8/15/2021
  Time: 5:06 PM
*/
package com.speingsecurity.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Users
{
    @Id
    @GeneratedValue(strategy=AUTO)
    private Long id;
    private String name;
    private String username;
    private String password;
    @ManyToMany(fetch= EAGER) //The relation E-R
    private Collection<Role> role;


}
