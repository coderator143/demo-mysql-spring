package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.ResponseBody;
import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.helpers.Constants;
import com.example.paytm.inpg.helpers.JwtUtil;
import com.example.paytm.inpg.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/token")
    public ResponseEntity<?> generateToken(@RequestBody User user) throws Exception {
        ResponseBody responseBody;
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), String.valueOf(user.getMobilenumber())
                    )
            );
        }
        catch (UsernameNotFoundException e) {
            responseBody = new ResponseBody("Username not found", "Token generation failed");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        catch (BadCredentialsException e) {
            responseBody = new ResponseBody("Bad credentials", "Token generation failed");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }

        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(user.getUsername());
        String token = this.jwtUtil.generateToken(userDetails);
        Constants.setAuthToken(token);
        responseBody = new ResponseBody(token, "user logged in");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
