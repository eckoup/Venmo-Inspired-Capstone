package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final PrintWriter printWriter = new PrintWriter(System.out); //added printwriter

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }


    public void printTransfers(Long id, Long accountFrom, Long accountTo, BigDecimal amount) {
        System.out.println("-------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------");
        System.out.println("Id: " + id);
        System.out.println("From: " + accountFrom);
        System.out.println("To: " + accountTo);
        System.out.println("Amount: $" + amount);

    }
    public String getUserInput(String prompt) {
        System.out.print(prompt+": ");
        System.out.flush();
        return scanner.nextLine();
    }
    public int getUserInputInteger(String prompt) {
        Integer result = null;
        do {
            System.out.print(prompt + ": ");
            System.out.flush();
            String userInput = scanner.nextLine();
            try {
                result = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                System.out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
            }
        } while (result == null);
        return result;
    }

    public void printApproveOrRejectOptions() {
        System.out.println("1: Approve");
        System.out.println("2: Reject");
       System.out.println("0: Don't approve or reject\n");
    }

    public void printTransferDetails(Transfer[] transfer) {
    }
}
