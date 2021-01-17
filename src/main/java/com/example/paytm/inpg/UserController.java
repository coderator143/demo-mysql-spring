package com.example.paytm.inpg;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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
            return new ResponseEntity<User>(user, OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public void add(@RequestBody User user) {
        userService.save(user);
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