package com.mycompany.chatappmaven;

import java.util.ArrayList;

public class Message {

    private String messageID;
    private int messageNum;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String flag; // Sent, Stored, Disregard

    // Static variables to keep track across the app
    public static int totalMessagesSent = 0;
    public static ArrayList<String> sentMessages = new ArrayList<>();

    // Constructor
    public Message(int num) {
        this.messageNum = num;
        this.messageID = generateID(num);
        this.flag = "Pending";
    }

    // Helper method to generate the ID using substring and the loop counter (num)
    private String generateID(int num) {
        // Create a 10 digit ID using the loop counter and random numbers
        String numStr = String.valueOf(num);
        if (numStr.length() > 2) {
            numStr = numStr.substring(0, 2); // make sure prefix is only 2 digits
        } else if (numStr.length() == 1) {
            numStr = "0" + numStr; // add a zero if it's a single digit
        }
        
        // generate random 8 digits
        long randomPart = (long) (Math.random() * 90000000L) + 10000000L;
        String randomStr = String.valueOf(randomPart);
        
        // Combine them to be 10 characters
        String finalID = numStr + randomStr;
        
        // Use substring just to be safe it's exactly 10
        return finalID.substring(0, 10);
    }

    // Method to check if ID is valid
    public boolean checkMessageID() {
        if (messageID.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }

    // Method to check recipient cell number
    public String checkRecipientCell() {
        String regex = "^\\+[0-9]{9,11}$";
        if (recipient != null && recipient.matches(regex)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Method to create the hash (first two ID numbers, colon, msg num, colon, first and last word)
    public String createMessageHash() {
        if (messageText == null || messageText.isEmpty()) {
            return "";
        }

        // get first two numbers of the ID
        String idPrefix = messageID.substring(0, 2);

        // split message by spaces to get first and last words
        String[] words = messageText.trim().split("\\s+");
        
        // remove punctuation from first and last words and make them uppercase
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "").toUpperCase();

        // format the hash
        this.messageHash = idPrefix + ":" + messageNum + ":" + firstWord + lastWord;
        return this.messageHash;
    }

    // Method for Send / Store / Disregard choice
    public String SentMessage(String choice) {
        if (choice.equals("1")) {
            this.flag = "Sent";
            totalMessagesSent++;
            sentMessages.add(this.toString());
            return "Message successfully sent.";
        } else if (choice.equals("2")) {
            this.flag = "Stored";
            // Store in JSON
            MessageStorage storage = new MessageStorage();
            storage.saveMessage(this);
            return "Message successfully stored.";
        } else if (choice.equals("3")) {
            this.flag = "Disregard";
            return "Message disregarded.";
        } else {
            return "Invalid choice.";
        }
    }

    // Print details in the exact order asked
    public String printDetails() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText;
    }

    // Show all sent messages
    public static String printMessages() {
        String result = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            result += sentMessages.get(i) + "\n\n";
        }
        return result;
    }

    // Return total sent
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Setters
    public void setRecipient(String rec) {
        this.recipient = rec;
    }

    public void setMessageText(String text) {
        this.messageText = text;
    }

    public String getFlag() {
        return flag;
    }
    
    public String getMessageText() {
        return messageText;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public String getMessageID() {
        return messageID;
    }
}