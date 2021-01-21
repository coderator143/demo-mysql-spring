package com.example.paytm.inpg.helpers;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.services.UserService;

// validating post requests in user controller
public class PostValidator {

    public static boolean isEmailValidated(String emailID, UserService userService) {
        return userService.findByEmailID(emailID).isEmpty();
    }

    public static boolean isUserNameValidated(String username, UserService userService) {
        return userService.findbyUserName(username).isEmpty();
    }

    public static boolean isMobileNumberValidated(long mobileNumber, UserService userService) {
        if(mobileNumber == 0) return true;
        return userService.findbyMobileNumber(mobileNumber).isEmpty();
    }

    public static String postResponseMessage(User user, UserService userService) {
        if(user.getUsername() == null) return "Username cannot be empty";
        else if(user.getFirstname() == null) return "Firstname cannot be empty";
        else if(user.getLastname() == null) return "Lastname cannot be empty";
        else if(!isEmailValidated(user.getEmailid(), userService))
            return "User with an identical email already exists";
        else if(!isUserNameValidated(user.getUsername(), userService))
            return "User with an identical username already exists";
        else if(!isMobileNumberValidated(user.getMobilenumber(), userService))
            return "User with an identical mobile number already exists";
        else return "";
    }
}
