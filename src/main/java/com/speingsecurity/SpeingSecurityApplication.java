package com.speingsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpeingSecurityApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(SpeingSecurityApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(UserService userService)
//    {
//        return args ->
//        {
//            userService.saveRole(new Role(null,"ROLE_USER"));
//            userService.saveRole(new Role(null,"ROLE_ADMIN"));
//            userService.saveRole(new Role(null,"ROLE_MANAGER"));
//            userService.saveRole(new Role(null,"ROLE_SUPERADMIN"));
//
//            userService.saveUser(new Users(null, "Diallo","alphaoumar","bonjour",new ArrayList<>()));
//            userService.saveUser(new Users(null, "Barry","barry","bonjour",new ArrayList<>()));
//            userService.saveUser(new Users(null, "Bah","bahoumar","bonjour",new ArrayList<>()));
//            userService.saveUser(new Users(null, "Sow","sowoumar","bonjour",new ArrayList<>()));
//
//            userService.addRoleToUser("alphaoumar","ROLE_USER");
//            userService.addRoleToUser("alphaoumar","ROLE_SUPERADMIN");
//            userService.addRoleToUser("barry","ROLE_ADMIN");
//            userService.addRoleToUser("bahoumar","ROLE_MANAGER");
//            userService.addRoleToUser("sowoumar","ROLE_SUPERADMIN");
//
//        };
//    }

    @Bean
    PasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
