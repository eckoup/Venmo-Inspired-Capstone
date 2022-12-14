package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public class TransferService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        BASE_URL = url;
    }

    public Transfer[] transfersList() {
        Transfer [] output = null;
        try {
            output = restTemplate.exchange(BASE_URL + "account/transfers/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            System.out.println("-------------------------------------------\r\n" +
                    "Transfers\r\n" +
                    "ID          From/To                 Amount\r\n" +
                    "-------------------------------------------\r\n");
            String fromOrTo = "";
            String name = "";
            for (Transfer i : output) {
                if (currentUser.getUser().getId() == i.getAccountFrom()) {
                    fromOrTo = "From: ";
                    name = i.getUserTo();
                } else {
                    fromOrTo = "To: ";
                    name = i.getUserFrom();
                }
                System.out.println(i.getTransferId() +"\t\t" + fromOrTo + name + "\t\t$" + i.getAmount());
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Please enter transfer ID to view details (Type 0 to cancel): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (Integer.parseInt(input) != 0) {
                boolean foundTransferId = false;
                for (Transfer i : output) {
                    if (Integer.parseInt(input) == i.getTransferId()) {
                        Transfer temp = restTemplate.exchange(BASE_URL + "transfers/" + i.getTransferId(), HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
                        foundTransferId = true;
                        System.out.println("--------------------------------------------\r\n" +
                                "Transfer Details\r\n" +
                                "--------------------------------------------\r\n" +
                                " Id: "+ temp.getTransferId() + "\r\n" +
                                " From: " + temp.getUserFrom() + "\r\n" +
                                " To: " + temp.getUserTo() + "\r\n" +
                                " Type: " + temp.getTransferType() + "\r\n" +
                                " Status: " + temp.getTransferStatus() + "\r\n" +
                                " Amount: $" + temp.getAmount());
                    }
                }
                if (!foundTransferId) {
                    System.out.println("Not a valid transfer ID");
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong...");
        }
        return output;
    }

    public void sendBucks() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        User[] users = null;
        Transfer transfer = new Transfer();
//        try {
            Scanner scanner = new Scanner(System.in);
            users = restTemplate.exchange(BASE_URL + "listusers/", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println("-------------------------------------------\r\n" +
                    "Users\r\n" +
                    "ID\t\tName\r\n" +
                    "-------------------------------------------");
            for (User i : users) {
                if (i.getId() != currentUser.getUser().getId()) {
                    System.out.println(i.getId() + "\t\t" + i.getUsername());
                }
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Enter ID of user you are sending to (0 to cancel): ");
            transfer.setAccountTo(Integer.parseInt(scanner.nextLine()));
            transfer.setAccountFrom(currentUser.getUser().getId());
            if (transfer.getAccountTo() != 0) {
                System.out.print("Enter amount: ");
                try {
                    transfer.setAmount(new BigDecimal(Double.parseDouble(scanner.nextLine())));
                } catch (NumberFormatException e) {
                    System.out.println("Error when entering amount");
                }
                //Error at Line 106, causing Try/Catch at line 110 to be thrown.
                String output = restTemplate.exchange(BASE_URL + "transfer/", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
                System.out.println(output);
            }
//        } catch (Exception e) {
//            System.out.println("Code error");
//        }
    }

    public void requestBucks() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        User[] users = null;
        Transfer transfer = new Transfer();
        try {
            Scanner scanner = new Scanner(System.in);
            users = restTemplate.exchange(BASE_URL + "listusers/", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println("-------------------------------------------\r\n" +
                    "Users\r\n" +
                    "ID\t\tName\r\n" +
                    "-------------------------------------------");
            for (User i : users) {
                if (i.getId() != currentUser.getUser().getId()) {
                    System.out.println(i.getId() + "\t\t" + i.getUsername());
                }
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Enter ID of user you are requesting from (0 to cancel): ");
            transfer.setAccountTo(currentUser.getUser().getId());
            transfer.setAccountFrom(Integer.parseInt(scanner.nextLine()));
            if (transfer.getAccountTo() != 0) {
                System.out.print("Enter amount: ");
                try {
                    transfer.setAmount(new BigDecimal(Double.parseDouble(scanner.nextLine())));
                } catch (NumberFormatException e) {
                    System.out.println("Error when entering amount");
                }
                String output = restTemplate.exchange(BASE_URL + "request/", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
                System.out.println(output);
            }
        } catch (Exception e) {
            System.out.println("Bad input.");
        }
    }

    public Transfer[] transfersRequestList() {
        Transfer [] output = null;
        String results;
        try {
            output = restTemplate.exchange(BASE_URL + "request/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            System.out.println("-------------------------------------------\r\n" +
                    "Pending Transfers\r\n" +
                    "ID          From/To                 Amount\r\n" +
                    "-------------------------------------------\r\n");
            String fromOrTo = "";
            String name = "";
            for (Transfer i : output) {
                if (currentUser.getUser().getId() == i.getAccountFrom()) {
                    fromOrTo = "From: ";
                    name = i.getUserTo();
                } else {
                    fromOrTo = "To: ";
                    name = i.getUserFrom();
                }
                System.out.println(i.getTransferId() +"\t\t" + fromOrTo + name + "\t\t$" + i.getAmount());
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Please enter transfer ID to approve/reject (0 to cancel): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (Integer.parseInt(input) != 0) {
                boolean foundTransferId = false;
                for (Transfer i : output) {
                    if (i.getAccountTo() != currentUser.getUser().getId()) {
                        if (Integer.parseInt(input) == i.getTransferId()) {
                            System.out.print("-------------------------------------------\r\n" +
                                    i.getTransferId() +"\t\t" + fromOrTo + name + "\t\t$" + i.getAmount() + "\r\n" +
                                    "1: Approve\r\n" +
                                    "2: Reject\r\n" +
                                    "0: Don't approve or reject\r\n" +
                                    "--------------------------\r\n" +
                                    "Please choose an option: ");
                            try {
                                int id = 1 + Integer.parseInt(scanner.nextLine());
                                if (id != 1) {
                                    results = restTemplate.exchange(BASE_URL + "transfer/status/" + id, HttpMethod.PUT, makeTransferEntity(i), String.class).getBody();
                                    System.out.println(results);
                                    foundTransferId = true;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Not a valid transfer option");
                            }
                            if (!foundTransferId) {
                                System.out.println("Not a valid transfer ID");
                            }
                        }
                    } else {
                        System.out.println("You can not approve/reject your own request.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong...");
        }
        return output;
    }

    public User[] getUsers() {
        User[] user = null;
        try {
            user = restTemplate.exchange(BASE_URL + "listusers", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            for (User i : user) {
                System.out.println(i);
            }
        } catch (RestClientResponseException e) {
            System.out.println("Error getting users");
        }
        return user;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

//    public boolean createTransfer(Transfer transfer){
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setBearerAuth(currentUser.getToken());
//            HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
//            transfer = restTemplate.postForObject(BASE_URL + "transfer", entity, Transfer.class);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            return false;
//
//        } return true;
//
//    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}





