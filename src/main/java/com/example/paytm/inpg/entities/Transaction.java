package com.example.paytm.inpg.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Transaction entity for transaction table
@Entity
public class Transaction {

    private int withuser, amount, user;
    private String id, mode, status;
    private long time;

    public Transaction() {}

    public Transaction(String id, int withuser, String mode, String status, int amount, long time, int user) {
        this.id = id;
        this.withuser = withuser;
        this.amount = amount;
        this.mode = mode;
        this.status = status;
        this.time = time;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWithuser() {
        return withuser;
    }

    public void setWithuser(int withuser) {
        this.withuser = withuser;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "withuser=" + withuser +
                ", amount=" + amount +
                ", user=" + user +
                ", id='" + id + '\'' +
                ", mode='" + mode + '\'' +
                ", status='" + status + '\'' +
                ", time=" + time +
                '}';
    }
}
