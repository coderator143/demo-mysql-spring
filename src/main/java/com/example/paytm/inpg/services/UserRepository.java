package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// by extending JpaRepository this interface contains all pre-created methods for logic of HTTP requests
public interface UserRepository extends JpaRepository<User, Integer> {

    // these methods are custom finder methods, i.e for custom queries for mysql
    // Here an example query is SELECT * FROM USER WHERE emailid = 'SOME_STRING'
    public List<User> findByEmailid(String emailid);

    public List<User> findByUsername(String username);

    public List<User> findByMobilenumber(long mobilenumber);
}
