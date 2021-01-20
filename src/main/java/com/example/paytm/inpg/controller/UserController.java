package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.services.UserService;
import com.example.paytm.inpg.helpers.PostValidator;
import com.example.paytm.inpg.helpers.PutValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.*;

// controller class for accepting HTTP Requests
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    // basically calling CRUD methods of the service class and specifying the response to return
    @GetMapping("/user")
    public List<User> list() {
        logger.log(Level.INFO, "list of all users returned");
        return userService.listAll();
    }

    @GetMapping(value = "/user", params = "userId")
    public ResponseEntity<User> get(@RequestParam("userId") Integer id) {
        try {
            User user = userService.get(id);
            ResponseEntity<User> r = new ResponseEntity<>(user, OK);
            logger.log(Level.INFO, "Read user successfully with id = "+id);
            return r;
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot read nonexistent user");
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<?> add(@RequestBody User user) {
        String msg = PostValidator.postResponseMessage(user, userService);
        if(msg != "") {
            logger.log(Level.INFO, msg);
            return new ResponseEntity<>(CONFLICT);
        }
        logger.log(Level.INFO, "User saved with id = "+user.getId());
        userService.save(user);
        return new ResponseEntity<>(OK);
    }

    @PutMapping(value = "/user", params = "userId")
    public ResponseEntity<?> update(@RequestBody User user, @RequestParam("userId") Integer id) {
        try {
            User existingUser = userService.get(id);
            if(!PutValidator.canBeUpdated(user, existingUser)) {
                logger.log(Level.INFO, "Only email and address can be updated");
                return new ResponseEntity<>(BAD_REQUEST);
            }
            logger.log(Level.INFO, "Updated user successfully with id = "+id);
            userService.save(user);
            return new ResponseEntity<>(OK);
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot update nonexistent user");
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/user", params = "userId")
    public ResponseEntity<?> delete(@RequestParam("userId") Integer id) {
        try {
            User existingUser = userService.get(id);
            logger.log(Level.INFO, "Deleted user successfully with id = "+id);
            userService.delete(id);
            return new ResponseEntity<>(OK);
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot delete nonexistent user");
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/user")
    public ResponseEntity<?> deleteAll() {
        logger.log(Level.INFO, "all users deleted");
        userService.deleteAll();
        return new ResponseEntity<>(OK);
    }
}