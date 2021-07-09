package com.example.radiologyclient.http;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserController {
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        String name;

        if (principal == null) {
            // not logged in
            name = null;
        } else {
            name = principal.getAttribute("name");
        }
        return Collections.singletonMap("name", name);
    }
}
