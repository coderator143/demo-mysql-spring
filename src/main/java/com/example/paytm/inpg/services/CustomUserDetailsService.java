package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        List<User> userList = userRepository.findByUsername(userName);
        if(userList.isEmpty())
            throw new UsernameNotFoundException("User not found with username: "+userName);
        User user = userList.get(0);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                String.valueOf(user.getMobilenumber()), new ArrayList<>());
    }
}
