package com.example.paytm.inpg.entities;

import javax.persistence.*;

// User entity for user table
@Entity
public class User {

    private int id;
    private String username, firstname, lastname, emailid, address1, address2;
    private long mobilenumber;
    private boolean haswallet;

    public User() {}

    public User(int id, String username, String firstname, String lastname, String emailid, String address1,
                String address2, long mobilenumber, boolean haswallet) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailid = emailid;
        this.address1 = address1;
        this.address2 = address2;
        this.mobilenumber = mobilenumber;
        this.haswallet = haswallet;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public long getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(long mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public boolean getHaswallet() {
        return haswallet;
    }

    public void setHaswallet(boolean haswallet) {
        this.haswallet = haswallet;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", emailid='" + emailid + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", mobilenumber=" + mobilenumber +
                ", haswallet=" + haswallet +
                '}';
    }
}
