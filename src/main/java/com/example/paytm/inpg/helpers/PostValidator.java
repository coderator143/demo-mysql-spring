package com.example.paytm.inpg.helpers;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
import com.example.paytm.inpg.services.UserService;
import com.example.paytm.inpg.services.WalletService;
import org.apache.commons.logging.Log;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

// validating post requests in user controller
public class PostValidator {

    private static Logger logger = Logger.getLogger("Wallet controller");

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

    public static List<User> walletPostValidate(long mobileNumber, UserService userService) {
        // if mobile number we get from request body is 0
        List<User> walletUser = new ArrayList<>();
        if(mobileNumber == 0) {
            Constants.WALLET_POST_MESSAGE = "Mobile number field is empty";
            return walletUser;
        }

        // getting a list of user with the specified mobile number
        walletUser = userService.findbyMobileNumber(mobileNumber);

        // if list is empty then the user with phone number doesn't exist
        if(walletUser.isEmpty()) {
            Constants.WALLET_POST_MESSAGE = "User with phone number "+ mobileNumber +" does not exist";
            return walletUser;
        }
        // if list has a user but he is not already registered for a wallet
        else if(walletUser.size() > 0 && walletUser.get(0).getHaswallet()) {
            walletUser.remove(0);
            Constants.WALLET_POST_MESSAGE = "User already has a wallet registered";
            return walletUser;
        }
        return walletUser;
    }

    public static void createSuccessfulWalletAccount(List<User> walletUser, UserService userService,
                                                     WalletService walletService) {
        // getting and setting user object from userList
        User user = walletUser.get(0);
        user.setHaswallet(true);

        // creating new wallet object for the user
        Wallet wallet = new Wallet();
        wallet.setBalance(0);
        wallet.setCreation(UtilityMethods.get_current_time());
        wallet.setOwner(user.getId());
        userService.save(user);
        walletService.save(wallet);
        Constants.WALLET_POST_MESSAGE = "Wallet created";
    }
}
