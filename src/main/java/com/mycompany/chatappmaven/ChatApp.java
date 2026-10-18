package com.mycompany.chatappmaven;

import java.util.Scanner;

public class ChatApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login loginSystem = new Login();

        System.out.println("=== Registration ===");

        // Get names
        System.out.print("Enter first name: ");
        String fName = input.nextLine();
        System.out.print("Enter last name: ");
        String lName = input.nextLine();
        loginSystem.setName(fName, lName);

        // Get and validate username (loops until valid)
        String user = "";
        boolean validUser = false;
        while (!validUser) {
            System.out.print("Enter username: ");
            user = input.nextLine();
            if (loginSystem.checkUserName(user)) {
                System.out.println("Username successfully captured.");
                validUser = true;
            } else {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        }

        // Get and validate password (loops until valid)
        String pass = "";
        boolean validPass = false;
        while (!validPass) {
            System.out.print("Enter password: ");
            pass = input.nextLine();
            if (loginSystem.checkPasswordComplexity(pass)) {
                System.out.println("Password successfully captured.");
                validPass = true;
            } else {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            }
        }
        
        // Get and validate cell (loops until valid)
        String cell = "";
        boolean validCell = false;
        while (!validCell) {
            System.out.print("Enter cell phone number: ");
            cell = input.nextLine();
            if (loginSystem.checkCellPhoneNumber(cell)) {
                System.out.println("Cell number successfully captured.");
                validCell = true;
            } else {
                System.out.println("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.");
            }
        }

        // Register
        System.out.println("\n" + loginSystem.registerUser(user, pass, cell));

        // --- LOGIN ---
        System.out.println("\n=== Login ===");
        boolean loggedIn = false;
        
        while (!loggedIn) {
            System.out.print("Enter username: ");
            String loginUser = input.nextLine();
            System.out.print("Enter password: ");
            String loginPass = input.nextLine();

            loggedIn = loginSystem.loginUser(loginUser, loginPass);
            System.out.println(loginSystem.returnLoginStatus(loggedIn));
            if (!loggedIn) {
                System.out.println("Please try again.\n");
            }
        }

        // --- PART 2 MENU ---
        System.out.println("\nWelcome to QuickChat.");
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                // Ask how many messages
                System.out.print("How many messages do you wish to enter? ");
                int numMessages = Integer.parseInt(input.nextLine());

                for (int i = 0; i < numMessages; i++) {
                    System.out.println("\n--- Message " + (i + 1) + " ---");
                    Message msg = new Message(i);

                    // Get Recipient
                    boolean validRecipient = false;
                    while (!validRecipient) {
                        System.out.print("Recipient: ");
                        msg.setRecipient(input.nextLine());
                        String result = msg.checkRecipientCell();
                        System.out.println(result);
                        if (result.equals("Cell phone number successfully captured.")) {
                            validRecipient = true;
                        }
                    }

                    // Get Message text
                    boolean validLength = false;
                    while (!validLength) {
                        System.out.print("Message: ");
                        msg.setMessageText(input.nextLine());
                        if (msg.getMessageText().length() <= 250) {
                            System.out.println("Message ready to send.");
                            validLength = true;
                        } else {
                            int excess = msg.getMessageText().length() - 250;
                            System.out.println("Message exceeds 250 characters by " + excess + "; please reduce the size.");
                        }
                    }

                    // Generate hash
                    msg.createMessageHash();

                    // Send/Store/Disregard
                    System.out.println("1) Send  2) Store  3) Disregard");
                    System.out.print("Choice: ");
                    String action = input.nextLine();
                    System.out.println(msg.SentMessage(action));

                    // If sent, print details
                    if (msg.getFlag().equals("Sent")) {
                        System.out.println("\n" + msg.printDetails());
                    }
                }
                System.out.println("Total messages sent: " + Message.returnTotalMessages());

            } else if (choice.equals("2")) {
                System.out.println("Coming Soon.");
            } else if (choice.equals("3")) {
                running = false;
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}