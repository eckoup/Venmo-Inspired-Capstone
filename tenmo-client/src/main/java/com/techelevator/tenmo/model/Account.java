package com.techelevator.tenmo.model;

public class Account {
    private Long accountId;
    private Long userId;
    private Balance balance;

    public Long getAccountId(AuthenticatedUser currentUser, Long id) {
        return accountId;
    }

    public Account (Balance balance){
        this.balance = balance;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId(AuthenticatedUser currentUser) {return userId;}

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Balance getBalance(AuthenticatedUser currentUser) {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

}
