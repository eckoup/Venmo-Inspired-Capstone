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

    private void viewCurrentBalance() {
        Balance balance = accountService.getBalance(currentUser);
        System.out.println("Your current account balance is:  $" + balance.getBalance());
    }

    private void viewTransferHistory() {
        Transfer[] transfer = transferService.getAllTransfers(currentUser, currentUser.getUser().getId());
        System.out.println("Your transfer history is as follows: ");

        Long currentUserAccountId = accountService.getAccountId(currentUser, currentUser.getUser().getId());
        for (Transfer transfers : transfer) {
            printTransferStubDetails(currentUser, transfers);
        }
        String transferIdChoice = console.getUserInput("\nPlease enter transfer ID to view details (0 to cancel)");
        Transfer transferChoice = validateTransferIdChoice(Integer.parseInt(transferIdChoice), transfer, currentUser);
        if (transferChoice != null) {
            consoleService.printTransferDetails(transfer);
        }


    }

    private Transfer validateTransferIdChoice(int transferIdChoice, Transfer[] transfers, AuthenticatedUser currentUser) {
        Transfer transferChoice = null;
        if (transferIdChoice != 0) {
            try {
                boolean validTransferIdChoice = false;
                for (Transfer transfer : transfers) {
                    if (transfer.getTransferId() == transferIdChoice) {
                        validTransferIdChoice = true;
                        transferChoice = transfer;
                        break;
                    }
                }
                if (!validTransferIdChoice) {
                    throw new Exception("Invalid Transfer Id Choice");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return transferChoice;
    }

    private void printTransferStubDetails(AuthenticatedUser currentUser, Transfer transfer) {
        String fromOrTo = "";
        Long accountFrom = transfer.getAccountFrom();
        Long accountTo = transfer.getAccountTo();
        if (accountService.getUserId(currentUser) == currentUser.getUser().getId()) {
            Long accountFromUserId = accountService.getUserId(currentUser);
            Long userFromName = accountService.getUserId(currentUser);
            fromOrTo = "From: " + userFromName;
        } else {
            Long accountToUserId = accountService.getUserId(currentUser);
            Long userToName = accountService.getUserId(currentUser);
            fromOrTo = "To: " + userToName;
        }
        console.printTransfers(transfer.getTransferTypeId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }


    private void viewPendingRequests() {
        Transfer[] transfers = transferService.getPendingTransfersByUserId(currentUser);
        System.out.println("-------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To          Amount");
        System.out.println("-------------------------------");

        for (Transfer transfer : transfers) {
            printTransferStubDetails(currentUser, transfer);
        }
        // TODO
        int transferIdChoice = console.getUserInputInteger("\nPlease enter transfer ID to approve/reject (0 to cancel)");
        Transfer transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
        if (transferChoice != null) {
            approveOrReject(transferChoice, currentUser);
        }

    }

    private void sendBucks() {
        Long users = accountService.getUserId(currentUser);
        printUserOptions(currentUser, users);

        int userIdChoice = console.getUserInputInteger("Enter the ID of the user you are sending to (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = console.getUserInput("Enter amount");
            transferService.createTransfer(currentUser);
        }

    }

    private void requestBucks() {
        Long users = accountService.getUserId(currentUser);
        printUserOptions(currentUser, users);
        int userIdChoice = console.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = console.getUserInput("Enter amount");
            transferService.createTransfer(currentUser);
        }

    }

    private void printUserOptions(AuthenticatedUser authenticatedUser, Long users) {

        System.out.println("-------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------------");
        System.out.println(users);
    }

    private boolean validateUserChoice(Long userIdChoice, String users, AuthenticatedUser currentUser) {
        if (userIdChoice != 0) {
            try {
                boolean validUserIdChoice = false;
//angry for each needs repair
                for (User user : users) {
                    if (userIdChoice == currentUser.getUser().getId()) {
                        throw new Exception("Invalid User Choice");
                    }
                    if (user.getId() == userIdChoice) {
                        validUserIdChoice = true;
                        break;
                    }
                }
                if (validUserIdChoice == false) {
                    throw new Exception("test");
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void approveOrReject(Transfer pendingTransfer, AuthenticatedUser authenticatedUser) {
        console.printApproveOrRejectOptions();
        long choice = console.getUserInputInteger("Please choose an option");

        if (choice != 0) {
            if (choice == 1) {
                long transferStatusId = transferStatusService.getTransferStatus(currentUser, "Approved").getTransferStatusId();
                pendingTransfer.setTransferStatusId(transferStatusId);
            } else if (choice == 2) {
                long transferStatusId = transferStatusService.getTransferStatus(currentUser, "Rejected").getTransferStatusId();
                pendingTransfer.setTransferStatusId(transferStatusId);
            } else {
                System.out.println("Invalid choice.");
            }
            transferService.updateTransfer(currentUser, pendingTransfer);
        }

    }
}

