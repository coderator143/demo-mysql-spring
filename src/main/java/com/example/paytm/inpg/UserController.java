package com.example.paytm.inpg;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

// this class is user for handling requests for Restful web services
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public List<User> list() {
        return userService.listAll();
    }

    @GetMapping("/user/{id}")
    public User get(@PathVariable Integer id) {
        return userService.get(id);
    }
}
