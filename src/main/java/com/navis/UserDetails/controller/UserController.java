package com.navis.UserDetails.controller;

import com.navis.UserDetails.JWT.JwtResponse;
import com.navis.UserDetails.JWT.JwtTokenUtil;
import com.navis.UserDetails.model.UserLogin;
import com.navis.UserDetails.model.UserSignUp;
import com.navis.UserDetails.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/SignUp")
    public UserSignUp signUp(@RequestBody UserSignUp employeeSignUp){
        System.out.println("in signUp");
        userService.saveOrUpdate(employeeSignUp);
        return employeeSignUp;
    }

    @GetMapping(value = "/test")
    public String test(){
        return "hello world";
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserLogin userLogin)throws Exception{
        System.out.println("in login");
        final UserDetails userDetails;
        String result = userService.authenticate(userLogin);
        if(result.equals(" logging in")) {
             userDetails = userService.loadUserByUsername(userLogin.getUserName());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
        }
        else {
            return ResponseEntity.ok(null);
        }

    }

    @GetMapping(value = "/listOfUsers")
    public List<UserSignUp> list(){
        return userService.getAllUsers();
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
