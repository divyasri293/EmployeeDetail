package com.navis.UserDetails.service;


import com.navis.UserDetails.model.UserLogin;
import com.navis.UserDetails.model.UserSignUp;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    public List<UserSignUp> getAllUsers();

    public UserSignUp getById(String emailId);

    public UserSignUp saveOrUpdate(UserSignUp userSignUp);

    public String encode(String password);

    public String authenticate(UserLogin userLogin);

    public void deleteById(String emailId);

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public void consume(String message);
}

