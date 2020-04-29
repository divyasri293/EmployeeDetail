package com.navis.UserDetails.service;


import com.navis.UserDetails.model.UserApiDetails;
import com.navis.UserDetails.model.UserLogin;
import com.navis.UserDetails.model.UserSignUp;
import com.navis.UserDetails.repository.ApiRepo;
import com.navis.UserDetails.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ApiRepo apiRepo;

    @Autowired
    UserApiDetails userApiDetails;

    @Override
    public List<UserSignUp> getAllUsers() {
        return (List<UserSignUp>) userRepo.findAll();
    }

    @Override
    public UserSignUp getById(String emailId) {
        return userRepo.findById(emailId).get();
    }

    @Override
    public UserSignUp saveOrUpdate(UserSignUp userSignUp) {
        UserSignUp employeeSignUp = new UserSignUp();
        if(!(userSignUp.getEmailId()).contains("@") && (userSignUp.getPassword().length()> 10)){
            System.out.println("invalid email id and password contains more than 10 characters");
            throw new SecurityException("invalid email id and password contains more than 10 characters");
        }
        else if (!(userSignUp.getEmailId().contains("@")))
        {
            System.out.println("invalid emailId");
            throw new SecurityException("invalid emailId");
        }
        else if (userSignUp.getPassword().length()>10){
            System.out.println("password contains more than 10 characters");
            throw new SecurityException("invalid password");
        }
        else{
            userSignUp.setPassword(encode(userSignUp.getPassword()));
             employeeSignUp = userRepo.save(userSignUp);
        }
        return employeeSignUp;
    }

        @Override
        @ExceptionHandler
        public String encode(String password){
            return bCryptPasswordEncoder.encode(password);

        }

    @Override
    public String authenticate(UserLogin userLogin) {
        UserSignUp userSignUp = userRepo.findByUserName(userLogin.getUserName());
        String result = " ";
            if (userSignUp == null) {
                System.out.println("don't have an account...signUp");
            } else if (bCryptPasswordEncoder.matches(userLogin.getPassword(), userSignUp.getPassword())) {
                result += "logging in";

            } else {
                result += "wrong password..reenter password";
                throw new SecurityException("wrong password");
            }
            return result;
    }
    @Override
    public void deleteById(String emailId) {
        userRepo.deleteById(emailId);
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSignUp user = userRepo.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                new ArrayList<>());
    }


    @KafkaListener(topics = "kafka_Example", groupId = "group_Id")
    @Override
    public void consume(String message) {
        System.out.println("consumed message:" + message);
        if(apiRepo.findByUserName(message) == null){
            userApiDetails.setUserName(message);
            apiRepo.save(userApiDetails);
        }
        UserApiDetails userApiDetails = apiRepo.findByUserName(message);
        Integer count = userApiDetails.getHitCount();
        String currentTime = getCurrentTimeUsingDate();
        if (count == null) {
            count = 1;
        } else {
            count++;
        }
        System.out.println(message + " hit count is " + count);
        userApiDetails.setHitCount(count);
        userApiDetails.setLastHitTime(currentTime);
        apiRepo.save(userApiDetails);
   }

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        return formattedDate;
    }
}
