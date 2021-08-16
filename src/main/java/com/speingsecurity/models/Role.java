/*
  Created by:oumar
  Project:speing-security
  Name:Role
  Date: 8/15/2021
  Time: 5:14 PM
*/
package com.speingsecurity.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Role
{
    @Id
    @GeneratedValue(strategy=AUTO)
    private Long id;
    private String name;

}
