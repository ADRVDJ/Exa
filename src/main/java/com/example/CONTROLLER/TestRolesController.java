package com.example.CONTROLLER;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesController {

    @GetMapping("/acessadmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accesAdmin(){
        return "hola admin";
    }
    @GetMapping("/acesUser")
    @PreAuthorize("hasRole('USER')")
    public String accesUser(){
        return "hola user";
    }

    @GetMapping("/acessinvited")
    @PreAuthorize("hasAnyRole('INVITED')")
    public String accesinvited(){
        return "hola invited";
    }

    /*@GetMapping("/acesUser")
    @PreAuthorize("hasRole('USER') or hasAnyRole('ADMIN')")//varios roles
    public String accesUser(){
        return "hola user";
    }

    @GetMapping("/acessinvited")
    @PreAuthorize("hasAnyRole('INVITED', 'ADMIN')")//TAMBIEN PARA VARIOS
    public String accesinvited(){
        return "hola invited";
    }*/


}
