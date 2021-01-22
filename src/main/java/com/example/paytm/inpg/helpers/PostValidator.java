package com.example.paytm.inpg.helpers;

import com.example.paytm.inpg.entities.Transaction;
import com.example.paytm.inpg.entities.TransactionRequestBody;
import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
import com.example.paytm.inpg.services.TransactionService;
import com.example.paytm.inpg.services.UserService;
import com.example.paytm.inpg.services.WalletService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// validating post requests in controllers
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

    public static List<User> walletPostValidate(long mobileNumber, UserService userService) {
        // if mobile number we get from request body is 0
        List<User> walletUser = new ArrayList<>();
        if(mobileNumber == 0) {
            Constants.setWalletPostMessage("Mobile number field is empty");
            return walletUser;
        }

        // getting a list of user with the specified mobile number
        walletUser = userService.findbyMobileNumber(mobileNumber);

        // if list is empty then the user with phone number doesn't exist
        if(walletUser.isEmpty()) {
            Constants.setWalletPostMessage("User with phone number "+ mobileNumber +" does not exist");
            return walletUser;
        }
        // if list has a user but he is not already registered for a wallet
        else if(walletUser.size() > 0 && walletUser.get(0).getHaswallet()) {
            walletUser.remove(0);
            Constants.setWalletPostMessage("User already has a wallet registered");
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
        Constants.setWalletPostMessage("Wallet created");
    }

    public static Map<Integer, Wallet> p2pPost(TransactionRequestBody requestBody,
                                                     UserService userService, WalletService walletService) {
        Map<Integer, Wallet> map = new HashMap<>();
        // payerUser and payeeUser are list of a user with payer and payee phone number
        List<User> payerUser = userService.findbyMobileNumber(requestBody.getPayer_phone_number());
        List<User> payeeUser = userService.findbyMobileNumber(requestBody.getPayee_phone_number());

        // if either of these lists are empty, that means user doesn't exist
        if(payeeUser.isEmpty() || payerUser.isEmpty()) {
            Constants.setP2pMessage("Either the payer or payee with this phone number doesn't exist");
            return map;
        }

        // getting ID of both the users
        int payerID = payerUser.get(0).getId(), payeeID = payeeUser.get(0).getId();
        int amount = requestBody.getAmount();

        // payerL and payeeL is a list of wallet of the payer and payee users
        List<Wallet> payerL= walletService.findByOwnerID(payerID);
        List<Wallet> payeeL = walletService.findByOwnerID(payeeID);

        // if either of the lists is empty, it means user doesn't have a registered account
        if(payeeL.isEmpty() || payerL.isEmpty()) {
            Constants.setP2pMessage("Either the payer or payee doesn't have a registered wallet");
            return map;
        }

        // getting wallet objects of the payer and payee
        Wallet payer = payerL.get(0), payee = payeeL.get(0);

        // if the payer has insufficient balance
        if(payer.getBalance() < amount) {
            Constants.setP2pMessage("Insufficient balance");
            return map;
        }
        map.put(1, payer);
        map.put(2, payee);
        return map;
    }

    public static void p2pCreate(Wallet payer, Wallet payee, int amount, WalletService walletService,
                                 TransactionService transactionService) {
        // updating and saving payer and payee wallets after the transaction
        payer.setBalance(payer.getBalance() - amount);
        payee.setBalance(payee.getBalance() + amount);
        walletService.save(payer); walletService.save(payee);
        int payerID = payer.getOwner(), payeeID = payee.getOwner();

        // creating and saving two transaction objects for payer and payee
        Transaction transactionPayer = new Transaction(), transactionPayee = new Transaction();
        transactionPayer.setUser(payerID); transactionPayer.setWithuser(payeeID);
        transactionPayer.setTime(System.currentTimeMillis()); transactionPayer.setMode("Payed");
        transactionPayer.setStatus("Completed"); transactionPayer.setAmount(amount);
        transactionPayee.setUser(payeeID); transactionPayee.setWithuser(payerID);
        transactionPayee.setTime(System.currentTimeMillis()); transactionPayee.setMode("Received");
        transactionPayee.setStatus("Completed"); transactionPayee.setAmount(amount);
        transactionService.save(transactionPayer);
        transactionService.save(transactionPayee);
    }
}