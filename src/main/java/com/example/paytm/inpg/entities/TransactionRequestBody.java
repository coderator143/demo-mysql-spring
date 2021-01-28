package com.example.paytm.inpg.entities;

// just an entity to make a json request body as an input for p2p transfer
public class TransactionRequestBody {

    private long payer_phone_number, payee_phone_number;
    private int amount;

    public TransactionRequestBody(long payer_phone_number, long payee_phone_number, int amount) {
        this.payer_phone_number = payer_phone_number;
        this.payee_phone_number = payee_phone_number;
        this.amount = amount;
    }

    public long getPayer_phone_number() {
        return payer_phone_number;
    }

    public void setPayer_phone_number(long payer_phone_number) {
        this.payer_phone_number = payer_phone_number;
    }

    public long getPayee_phone_number() {
        return payee_phone_number;
    }

    public void setPayee_phone_number(long payee_phone_number) {
        this.payee_phone_number = payee_phone_number;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionRequestBody{" +
                "payer_phone_number=" + payer_phone_number +
                ", payee_phone_number=" + payee_phone_number +
                ", amount=" + amount +
                '}';
    }
}
