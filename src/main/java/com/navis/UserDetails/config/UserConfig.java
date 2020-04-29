package com.navis.UserDetails.config;


import com.navis.UserDetails.model.UserApiDetails;
import com.navis.UserDetails.model.UserLogin;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserApiDetails userApiDetails(){
        return new UserApiDetails();
    }
}
