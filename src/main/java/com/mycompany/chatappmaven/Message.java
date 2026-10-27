package com.mycompany.chatappmaven;

import java.util.ArrayList;

public class Message {

    private String messageID;
    private int messageNum;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String flag; // Sent, Stored, Disregard

    // --- PART 3 ARRAYS (The "Memory" of the app) ---
    public static ArrayList<Message> sentMessages = new ArrayList<>();
    public static ArrayList<Message> disregardedMessages = new ArrayList<>();
    public static ArrayList<Message> storedMessages = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIds = new ArrayList<>();

    // Constructor for the app
    public Message(int num) {
        this.messageNum = num;
        this.messageID = generateID(num);
        this.flag = "Pending";
    }

    // Constructor for the unit tests (so we can force a specific ID)
    public Message(int num, String forcedID) {
        this.messageNum = num;
        this.messageID = forcedID;
        this.flag = "Pending";
    }

    private String generateID(int num) {
        String numStr = String.valueOf(num);
        if (numStr.length() > 2) {
            numStr = numStr.substring(0, 2);
        } else if (numStr.length() == 1) {
            numStr = "0" + numStr;
        }
        long randomPart = (long) (Math.random() * 90000000L) + 10000000L;
        String finalID = numStr + String.valueOf(randomPart);
        return finalID.substring(0, 10);
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        String regex = "^\\+[0-9]{9,11}$";
        if (recipient != null && recipient.matches(regex)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String createMessageHash() {
        if (messageText == null || messageText.isEmpty()) return "";
        String idPrefix = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "").toUpperCase();
        
        this.messageHash = idPrefix + ":" + messageNum + ":" + firstWord + lastWord;
        return this.messageHash;
    }

    // This now populates the arrays based on the user's choice
    public String SentMessage(String choice) {
        if (choice.equals("1")) {
            this.flag = "Sent";
            sentMessages.add(this);
            messageHashes.add(this.messageHash);
            messageIds.add(this.messageID);
            return "Message successfully sent.";
        } else if (choice.equals("2")) {
            this.flag = "Stored";
            storedMessages.add(this);
            messageHashes.add(this.messageHash);
            messageIds.add(this.messageID);
            MessageStorage storage = new MessageStorage();
            storage.saveMessage(this);
            return "Message successfully stored.";
        } else if (choice.equals("3")) {
            this.flag = "Disregard";
            disregardedMessages.add(this);
            messageHashes.add(this.messageHash);
            messageIds.add(this.messageID);
            return "Message disregarded.";
        } else {
            return "Invalid choice.";
        }
    }

    public String printDetails() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText;
    }

    // ====================================================
    // --- PART 3 METHODS (The 6 features from the PDF) ---
    // ====================================================

    // a. Display sender and recipient of all stored messages
    public static String displayStoredSenderAndRecipient() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        String result = "";
        for (Message msg : storedMessages) {
            result += "Recipient: " + msg.recipient + "\n";
        }
        return result;
    }

    // b. Display the longest stored message
    public static String getLongestStoredMessage() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        Message longestMsg = storedMessages.get(0);
        for (Message msg : storedMessages) {
            if (msg.messageText.length() > longestMsg.messageText.length()) {
                longestMsg = msg;
            }
        }
        return longestMsg.messageText;
    }

        // c. Search for a message ID and display recipient and message
    public static String searchMessageByID(String id) {
        // Search in stored messages first
        for (Message msg : storedMessages) {
            if (msg.messageID.equals(id)) {
                return "Recipient: " + msg.recipient + "\nMessage: " + msg.messageText;
            }
        }
        // Then search in sent messages (like Message 4 from the PDF)
        for (Message msg : sentMessages) {
            if (msg.messageID.equals(id)) {
                return "Recipient: " + msg.recipient + "\nMessage: " + msg.messageText;
            }
        }
        return "Message ID not found in stored messages.";
    }

    // d. Search all messages (sent or stored) for a particular recipient
    public static String searchMessagesByRecipient(String recipientNum) {
        String result = "";
        boolean found = false;
        
        for (Message msg : sentMessages) {
            if (msg.recipient.equals(recipientNum)) {
                result += msg.messageText + "\n";
                found = true;
            }
        }
        for (Message msg : storedMessages) {
            if (msg.recipient.equals(recipientNum)) {
                result += msg.messageText + "\n";
                found = true;
            }
        }
        
        if (!found) return "No messages found for this recipient.";
        return result.trim();
    }

    // e. Delete a message using the message hash
    public static String deleteMessageByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).messageHash.equals(hash)) {
                String deletedText = storedMessages.get(i).messageText;
                storedMessages.remove(i);
                MessageStorage storage = new MessageStorage();
                storage.rewriteStoredMessages(); // Update the JSON file
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    // f. Display report of all sent messages
    public static String generateReport() {
        if (sentMessages.isEmpty()) return "No sent messages to report.";
        String result = "";
        for (Message msg : sentMessages) {
            result += "Message Hash: " + msg.messageHash + "\n";
            result += "Recipient: " + msg.recipient + "\n";
            result += "Message: " + msg.messageText + "\n\n";
        }
        return result.trim();
    }

    // Helper to clear arrays for testing
    public static void resetArrays() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIds.clear();
    }

    // Setters and Getters
    public void setRecipient(String rec) { this.recipient = rec; }
    public void setMessageText(String text) { this.messageText = text; }
    public String getFlag() { return flag; }
    public String getMessageText() { return messageText; }
    public String getRecipient() { return recipient; }
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
}