package org.bitvault.appstore.cloud.user.common.controller;

import org.bitvault.appstore.cloud.security.auth.JwtAuthenticationToken;
import org.bitvault.appstore.cloud.security.auth.jwt.model.UserContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileEndpoint {
    @RequestMapping(value="/rest/api/me", method=RequestMethod.GET)
    public @ResponseBody UserContext get(JwtAuthenticationToken token) {
        return (UserContext) token.getPrincipal();
    }
}
