package com.fiuni.authapi.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {

    @GetMapping()
    public String hello() {
        return "Hello from the private API";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin() {
        return "Hello from the admin API";
    }

    @GetMapping("/Client")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String client() {
        return "Hello from the client API";
    }

    @GetMapping("/freelancer")
    @PreAuthorize("hasRole('ROLE_FREELANCER')")
    public String freelancer() {
        return "Hello from the freelancer API";
    }
}
