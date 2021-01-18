package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Using all pre-created methods of UserRepository
    public List<User> listAll() { return userRepository.findAll(); }

    public void save(User user) { userRepository.save(user); }

    public User get(Integer id) { return userRepository.findById(id).get(); }

    public void delete(Integer id) { userRepository.deleteById(id); }

    public List<User> findByEmailID(String emailid) { return userRepository.findByEmailid(emailid); }

    public List<User> findbyUserName(String username) { return userRepository.findByUsername(username); }

    public List<User> findbyMobileNumber(long mobilenumber) {
        return userRepository.findByMobilenumber(mobilenumber);
    }
}
