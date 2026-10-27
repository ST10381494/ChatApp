package com.mycompany.chatappmaven;

import java.util.Scanner;

public class ChatApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login loginSystem = new Login();

        System.out.println("=== Registration ===");

        // --- PART 1: REGISTRATION ---
        System.out.print("Enter first name: ");
        String fName = input.nextLine();
        System.out.print("Enter last name: ");
        String lName = input.nextLine();
        loginSystem.setName(fName, lName);

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

        System.out.println("\n" + loginSystem.registerUser(user, pass, cell));

        // --- PART 1: LOGIN ---
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

        // --- PART 2 & 3: MAIN MENU ---
        System.out.println("\nWelcome to QuickChat.");
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages"); 
            System.out.println("4) Quit");
            System.out.print("Choose option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                sendMessages(input);
            } else if (choice.equals("2")) {
                System.out.println("Coming Soon.");
            } else if (choice.equals("3")) {
                storedMessagesMenu(input); 
            } else if (choice.equals("4")) {
                running = false;
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    // --- PART 2: SEND MESSAGES ---
    private static void sendMessages(Scanner input) {
        System.out.print("How many messages do you wish to enter? ");
        int numMessages = Integer.parseInt(input.nextLine());

        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " ---");
            Message msg = new Message(i);

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

            msg.createMessageHash();

            System.out.println("1) Send  2) Store  3) Disregard");
            System.out.print("Choice: ");
            String action = input.nextLine();
            System.out.println(msg.SentMessage(action));

            if (msg.getFlag().equals("Sent") || msg.getFlag().equals("Stored")) {
                System.out.println("\n" + msg.printDetails());
            }
        }
        System.out.println("Total messages sent: " + Message.sentMessages.size());
    }

    // --- PART 3: STORED MESSAGES MENU ---
    private static void storedMessagesMenu(Scanner input) {
        // READ JSON FILE INTO ARRAY (Rubric: Read JSON file into an array and display it)
        MessageStorage storage = new MessageStorage();
        Message.storedMessages = storage.loadStoredMessages();

        boolean inSubMenu = true;
        
        while (inSubMenu) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search messages for a particular recipient");
            System.out.println("5) Delete a message using the message hash");
            System.out.println("6) Display report of sent messages");
            System.out.println("7) Back to Main Menu");
            System.out.print("Choose option: ");
            
            String subChoice = input.nextLine();

            if (subChoice.equals("1")) {
                System.out.println("\n" + Message.displayStoredSenderAndRecipient());
            } else if (subChoice.equals("2")) {
                System.out.println("\nLongest Message: " + Message.getLongestStoredMessage());
            } else if (subChoice.equals("3")) {
                System.out.print("Enter Message ID to search: ");
                String id = input.nextLine();
                System.out.println("\n" + Message.searchMessageByID(id));
            } else if (subChoice.equals("4")) {
                System.out.print("Enter recipient cell number: ");
                String rec = input.nextLine();
                System.out.println("\n" + Message.searchMessagesByRecipient(rec));
            } else if (subChoice.equals("5")) {
                System.out.print("Enter Message Hash to delete: ");
                String hash = input.nextLine();
                System.out.println("\n" + Message.deleteMessageByHash(hash));
            } else if (subChoice.equals("6")) {
                System.out.println("\n--- Message Report ---");
                System.out.println(Message.generateReport());
            } else if (subChoice.equals("7")) {
                inSubMenu = false;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}