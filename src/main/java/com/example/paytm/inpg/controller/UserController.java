package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.services.UserService;
import helpers.PostValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import static org.springframework.http.HttpStatus.*;

// controller class for accepting HTTP Requests
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    private PostValidator postValidator = new PostValidator();

    // basically calling CRUD methods of the service class and specifying the response to return
    @GetMapping("/user")
    public List<User> list() {
        return userService.listAll();
    }

    @GetMapping(value = "/user", params = "userId")
    public ResponseEntity<User> get(@RequestParam("userId") Integer id) {
        try {
            User user = userService.get(id);
            return new ResponseEntity<User>(user, OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public String add(@RequestBody User user) {
        String msg = postValidator.postResponseMessage(user, userService);
        if(msg != "") return msg;
        userService.save(user);
        return "User saved";
    }

    @PutMapping(value = "/user", params = "userId")
    public ResponseEntity<?> update(@RequestBody User user, @RequestParam("userId") Integer id) {
        try {
            User existingUser = userService.get(id);
            userService.save(user);
            return new ResponseEntity<>(OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/user", params = "userId")
    public ResponseEntity<?> delete(@RequestParam("userId") Integer id) {
        try {
            User existingUser = userService.get(id);
            userService.delete(id);
            return new ResponseEntity<>(OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}