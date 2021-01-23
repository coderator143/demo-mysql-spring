package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.services.UserService;
import com.example.paytm.inpg.helpers.PostValidator;
import com.example.paytm.inpg.helpers.PutValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.paytm.inpg.entities.ResponseBody;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.*;

// user controller class for accepting and managing HTTP Requests
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    // basically calling CRUD methods of the service class and specifying the response to return
    @GetMapping("/loginUser")
    public String loginUser() {
        return "User logged in";
    }

    @GetMapping("/user")
    public List<User> list() {
        logger.log(Level.INFO, "list of all users returned");
        return userService.listAll();
    }

    @GetMapping(value = "/user", params = "userId")
    public ResponseEntity<?> get(@RequestParam("userId") Integer id) {
        try {
            User user = userService.get(id);
            ResponseEntity<User> r = new ResponseEntity<>(user, OK);
            logger.log(Level.INFO, "Read user successfully with id = "+id);
            return r;
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot read nonexistent user");
            ResponseBody responseBody = new ResponseBody("Cannot read nonexistent user",
                    "Not found");
            return new ResponseEntity<>(responseBody, NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseBody> add(@RequestBody User user) {
        String msg = PostValidator.postResponseMessage(user, userService);
        ResponseBody responseBody;
        if(msg != "") {
            logger.log(Level.INFO, msg);
            responseBody = new ResponseBody(msg, "undesired input");
            return new ResponseEntity<>(responseBody, OK);
        }
        logger.log(Level.INFO, "User saved with id = "+user.getId());
        responseBody = new ResponseBody("User saved with id = "+user.getId(), "OK");
        userService.save(user);
        return new ResponseEntity<>(responseBody, OK);
    }

    @PutMapping(value = "/user", params = "userId")
    public ResponseEntity<ResponseBody> update(@RequestBody User user, @RequestParam("userId") Integer id) {
        ResponseBody responseBody;
        try {
            User existingUser = userService.get(id);
            if(!PutValidator.canBeUpdated(user, existingUser)) {
                logger.log(Level.INFO, "Only email and address can be updated");
                responseBody = new ResponseBody("Only email and address can be updated",
                        "undesired input");
                return new ResponseEntity<>(responseBody, OK);
            }
            logger.log(Level.INFO, "Updated user successfully with id = "+id);
            responseBody = new ResponseBody("Updated user successfully with id = "+id, "OK");
            userService.save(user);
            return new ResponseEntity<>(responseBody, OK);
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot update nonexistent user");
            responseBody = new ResponseBody("Cannot update nonexistent user", "Not found");
            return new ResponseEntity<>(responseBody, NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/user", params = "userId")
    public ResponseEntity<ResponseBody> delete(@RequestParam("userId") Integer id) {
        ResponseBody responseBody;
        try {
            User existingUser = userService.get(id);
            logger.log(Level.INFO, "Deleted user successfully with id = "+id);
            responseBody = new ResponseBody("Deleted user successfully with id = "+id, "OK");
            userService.delete(id);
            return new ResponseEntity<>(responseBody, OK);
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot delete nonexistent user");
            responseBody = new ResponseBody("Cannot delete nonexistent user", "Not found");
            return new ResponseEntity<>(responseBody, NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/user")
    public ResponseEntity<?> deleteAll() {
        logger.log(Level.INFO, "all users deleted");
        ResponseBody responseBody = new ResponseBody("all users deleted", "OK");
        userService.deleteAll();
        return new ResponseEntity<>(responseBody, OK);
    }
}