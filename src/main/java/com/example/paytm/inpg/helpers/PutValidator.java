package com.example.paytm.inpg.helpers;

import com.example.paytm.inpg.entities.User;

// validating put requests for user controller
public class PutValidator {

    public static boolean canBeUpdated(User newUser, User existingUser) {
        return newUser.getUsername().equalsIgnoreCase(existingUser.getUsername()) &&
               newUser.getFirstname().equalsIgnoreCase(existingUser.getFirstname()) &&
               newUser.getLastname().equalsIgnoreCase(existingUser.getLastname()) &&
               newUser.getMobilenumber() == existingUser.getMobilenumber();
    }
}
