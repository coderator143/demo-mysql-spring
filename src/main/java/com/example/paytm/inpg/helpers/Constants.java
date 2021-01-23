package com.example.paytm.inpg.helpers;

public class Constants {

    private static String WALLET_POST_MESSAGE = "";
    private static String WALLET_PUT_MESSAGE = "";
    private static String P2P_MESSAGE = "";
    private static String authToken = "";

    public static String getWalletPostMessage() {
        return WALLET_POST_MESSAGE;
    }

    public static void setWalletPostMessage(String walletPostMessage) {
        WALLET_POST_MESSAGE = walletPostMessage;
    }

    public static String getWalletPutMessage() {
        return WALLET_PUT_MESSAGE;
    }

    public static void setWalletPutMessage(String walletPutMessage) {
        WALLET_PUT_MESSAGE = walletPutMessage;
    }

    public static String getP2pMessage() {
        return P2P_MESSAGE;
    }

    public static void setP2pMessage(String p2pMessage) {
        P2P_MESSAGE = p2pMessage;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String jwtToken) {
        authToken = jwtToken;
    }
}
