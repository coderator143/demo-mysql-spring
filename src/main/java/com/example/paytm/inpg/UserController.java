package com.example.paytm.inpg;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
// this class is user for handling requests for Restful web services
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public List<User> list() {
        return userService.listAll();
    }

    @GetMapping(value = "/user", params = "userId")
    public ResponseEntity<User> get(@RequestParam("userId") Integer id) {
        try {
            User user = userService.get(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public void add(@RequestBody User user) {
        userService.save(user);
    }
}