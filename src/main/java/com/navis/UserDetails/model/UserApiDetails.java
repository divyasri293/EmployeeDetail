package com.navis.UserDetails.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "api_details")
public class UserApiDetails {

    @Id
    @Column(name = "user_name")
    private String userName;

    @Column(columnDefinition = "integer default 0",name = "hit_count")
    private Integer hitCount;

    @Column(name = "last_hit_time")
    private String lastHitTime;

}
