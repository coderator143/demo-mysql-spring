package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
import com.example.paytm.inpg.helpers.UtilityMethods;
import com.example.paytm.inpg.services.UserService;
import com.example.paytm.inpg.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @PostMapping("/wallet")
    public ResponseEntity<?> add(@RequestBody User userBody) {
        long mobileNumber = userBody.getMobilenumber();
        if(mobileNumber == 0) {
            logger.log(Level.INFO, "Mobile number field is empty");
            return new ResponseEntity<>(BAD_REQUEST);
        }
        User user = userService.findbyMobileNumber(mobileNumber).get(0);
        user.setHaswallet(true);
        String date_created = UtilityMethods.get_current_time();
        Wallet wallet = new Wallet(1, 0, user.getId(), date_created);
        userService.save(user);
        walletService.save(wallet);
        return new ResponseEntity<>(OK);
    }
}
