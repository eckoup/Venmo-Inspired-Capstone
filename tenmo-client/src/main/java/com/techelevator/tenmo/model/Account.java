package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private Long accountId;
    private int userId;
    private Balance balance;

    public Long getAccountId() {
        return accountId;
    }
// need to add a constructor here that initializes balance object
    public Account (Balance balance){
        this.balance = balance;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Balance getBalance(AuthenticatedUser currentUser) {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}
