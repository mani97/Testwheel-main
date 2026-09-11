package com.aishu.spring_security.service;

import com.aishu.spring_security.dao.UserPrinciple;


import com.aishu.spring_security.Repository.UserRepo;
import com.aishu.spring_security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailService implements UserDetailsService{

    @Autowired
    private UserRepo repo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
       return new UserPrinciple(user);
    }}

