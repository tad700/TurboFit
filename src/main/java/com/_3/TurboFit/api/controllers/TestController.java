package com._3.TurboFit.api.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
@GetMapping("test/user")
    public String getUserData(Authentication authentication){
    String username = authentication.getName();
    String roles = authentication.getAuthorities().toString();

    return String.format("Hello, %s! You accesed a secured User endpoint. Your role is %s",username,roles);
}

    @GetMapping("/test/adminData")
    public String getAdminData() {
        return "SUCCESS: You have access to sensitive ADMIN data.";
    }

    @GetMapping("/test/public")
    public String getPublicData() {

        return "This is public data, accessible without a token.";
    }

}
