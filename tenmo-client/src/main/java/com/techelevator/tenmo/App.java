package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.RestTransferService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080";

    private ConsoleService consoleService = new ConsoleService();
    private AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private Account accountService;
    private RestTransferService transferService;



    public static void main(String[] args) {
        App app = new App(new ConsoleService(), new AuthenticationService(API_BASE_URL));
        app.run();
    }
    public App(ConsoleService console, AuthenticationService authenticationService) {
        this.console = console;
        this.authenticationService = authenticationService;
        this.accountService = new Account(new Balance(new BigDecimal(1000.0)));
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    //completed getBalance method
	private void viewCurrentBalance() {
        Balance balance = accountService.getBalance(currentUser);
        System.out.println("Your current account balance is:  $" + balance.getBalance());
	}

	private void viewTransferHistory() {
        Transfer[] transfer = transferService.getAllTransfers(currentUser, currentUser.getUser().getId());
        System.out.println("Your transfer history is as follows: " );

        Long currentUserAccountId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
        for(Transfer transfers: transfer) {
            printTransferStubDetails(currentUser, transfers);
        }
//        int transferIdChoice = console.getUserInputInteger("\nPlease enter transfer ID to view details (0 to cancel)");
//        Transfer transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
//        if(transferChoice != null) {
//            printTransferDetails(currentUser, transferChoice);
//        }


	}

    private void printTransferStubDetails(AuthenticatedUser currentUser, Transfer transfer) {
        String fromOrTo = "";
        Long accountFrom = transfer.getAccountFrom();
        Long accountTo = transfer.getAccountTo();
        if (accountService.getAccountByUserId(currentUser, accountTo).getUserId() == currentUser.getUser().getId()) {
            Long accountFromUserId = accountService.getAccountByUserId(currentUser, accountFrom).getUserId();
            Long userFromName = accountService.getAccountByUserId(currentUser, accountFromUserId).getUserId();
            fromOrTo = "From: " + userFromName;
        } else {
            long accountToUserId = accountService.getAccountByUserId(currentUser, accountTo).getUserId();
            Long userToName = accountService.getAccountByUserId(currentUser, accountToUserId).getUserId();
            fromOrTo = "To: " + userToName;
        }

        console.printTransfers(transfer.getTransferId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferTypeId(),  transfer.getAmount());
    }


    private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
     //Todo
		
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
