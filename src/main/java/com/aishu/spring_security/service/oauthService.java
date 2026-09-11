package com.aishu.spring_security.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;


@Service
public class oauthService {


    @GetMapping("/whoami")
    public String whoami(@AuthenticationPrincipal OAuth2User principal,
                         OAuth2AuthenticationToken authToken) {
        String provider = authToken.getAuthorizedClientRegistrationId();
        // "google" or "github" depending on login
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String picture = principal.getAttribute("picture");

        return "Signed in via " + provider + " as " + name + " (" + email + ")";
    }


    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal,
                                    Authentication authentication) {
        Map<String, Object> userDetails = new HashMap<>();

        // Extract profile info
        userDetails.put("username", principal.getAttribute("name"));
        userDetails.put("email", principal.getAttribute("email"));
        userDetails.put("picture", principal.getAttribute("picture"));

        // Extract tokens
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                "google", authentication.getName());

        userDetails.put("access_token", client.getAccessToken().getTokenValue());
        assert client.getRefreshToken() != null;
        userDetails.put("refresh_token", client.getRefreshToken().getTokenValue());

        return userDetails;
    }



}
