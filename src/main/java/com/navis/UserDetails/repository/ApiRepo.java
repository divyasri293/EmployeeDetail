package com.navis.UserDetails.repository;

import com.navis.UserDetails.model.UserApiDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepo extends JpaRepository<UserApiDetails,String> {
    UserApiDetails findByUserName(String userName);
}
