package com.example.paytm.inpg.helpers;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
import com.example.paytm.inpg.services.dataservice.WalletService;
import java.util.List;

// validating put requests for user controller
public class PutValidator {

    public static boolean canBeUpdated(User newUser, User existingUser) {
        return newUser.getUsername().equalsIgnoreCase(existingUser.getUsername()) &&
               newUser.getFirstname().equalsIgnoreCase(existingUser.getFirstname()) &&
               newUser.getLastname().equalsIgnoreCase(existingUser.getLastname()) &&
               newUser.getMobilenumber() == existingUser.getMobilenumber();
    }

    public static List<Wallet> canBalanceBeAdded(WalletService walletService, Integer id,
                                                 Wallet balanceWallet) {
        // find list of wallet by userID
        List<Wallet> wallets = walletService.findByOwnerID(id);

        // if wallet list is empty, user doesn't exist
        if(wallets.isEmpty()) {
            Constants.setWalletPutMessage("User does not exist");
            return wallets;
        }

        // getting wallet object from list and then balance
        Wallet wallet = wallets.get(0);
        int balance = balanceWallet.getBalance();

        // adding balance = 0 is insignificant, less than 0 is not possible
        if(balance < 1) {
            wallets.remove(0);
            Constants.setWalletPutMessage("Cannot add balance <= 0");
            return wallets;
        }
        return wallets;
    }
}