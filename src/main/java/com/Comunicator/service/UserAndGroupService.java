package com.Comunicator.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserAndGroupService {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<Map<String,Object>> fetchAll(String myId) {
        List<Map<String,Object>> getAllUser=jdbcTemplate.queryForList("SELECT * FROM user_data WHERE id!=?",myId);

        return getAllUser;

    }

}
