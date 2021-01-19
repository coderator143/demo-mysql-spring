package helpers;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.services.UserService;

public class PostValidator {

    public boolean isEmailValidated(String emailID, UserService userService) {
        return userService.findByEmailID(emailID).isEmpty();
    }

    public boolean isUserNameValidated(String username, UserService userService) {
        return userService.findbyUserName(username).isEmpty();
    }

    public boolean isMobileNumberValidated(long mobileNumber, UserService userService) {
        return userService.findbyMobileNumber(mobileNumber).isEmpty();
    }

    public String postResponseMessage(User user, UserService userService) {
        if(!isEmailValidated(user.getEmailid(), userService))
            return "User with an identical email already exists";
        else if(!isUserNameValidated(user.getUsername(), userService))
            return "User with an identical username already exists";
        else if(!isMobileNumberValidated(user.getMobilenumber(), userService))
            return "User with an identical mobile number already exists";
        else return "";
    }
}
