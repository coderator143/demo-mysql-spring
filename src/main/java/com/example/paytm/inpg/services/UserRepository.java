package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    public List<User> findByEmailid(String emailid);

    public List<User> findByUsername(String username);

    public List<User> findByMobilenumber(long mobilenumber);
}
