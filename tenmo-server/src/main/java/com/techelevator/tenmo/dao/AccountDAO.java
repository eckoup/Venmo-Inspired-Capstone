package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan
public interface AccountDAO {


    Balance getBalance(Long userId);


    Account getAccountByUserID(int userId);


    Account getAccountByAccountID(int accountId);


    void updateAccount(Account accountToUpdate);

}
