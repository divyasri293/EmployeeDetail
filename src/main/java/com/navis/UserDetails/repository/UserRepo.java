package com.navis.UserDetails.repository;


import com.navis.UserDetails.model.UserSignUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserSignUp,String> {
     UserSignUp findByUserName(String userName);

}
