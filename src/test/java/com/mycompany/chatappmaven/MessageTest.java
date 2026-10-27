package com.mycompany.chatappmaven;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @BeforeEach
    public void setUp() {
        // Clear all arrays and delete the JSON file before each test
        Message.resetArrays();
        java.io.File file = new java.io.File("messages.json");
        if (file.exists()) {
            file.delete();
        }
    }

    // Helper method to populate the arrays with the PDF test data
    private void populateTestData() {
        // Message 1: Sent
        Message msg1 = new Message(0, "0012345678");
        msg1.setRecipient("+27834557896");
        msg1.setMessageText("Did you get the cake?");
        msg1.createMessageHash();
        msg1.SentMessage("1");

        // Message 2: Stored
        Message msg2 = new Message(1, "1112345678");
        msg2.setRecipient("+27838884567");
        msg2.setMessageText("Where are you? You are late! I have asked you to be on time.");
        msg2.createMessageHash();
        msg2.SentMessage("2");

        // Message 3: Disregard
        Message msg3 = new Message(2, "2212345678");
        msg3.setRecipient("+27834484567");
        msg3.setMessageText("Yohoooo, I am at your gate.");
        msg3.createMessageHash();
        msg3.SentMessage("3");

        // Message 4: Sent (Developer)
        Message msg4 = new Message(3, "0838884567");
        msg4.setRecipient("0838884567");
        msg4.setMessageText("It is dinner time !");
        msg4.createMessageHash();
        msg4.SentMessage("1");
        
        // Message 5: Stored
        Message msg5 = new Message(4, "4412345678");
        msg5.setRecipient("+27838884567");
        msg5.setMessageText("Ok, I am leaving without you.");
        msg5.createMessageHash();
        msg5.SentMessage("2");
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        populateTestData();
        // The rubric says the array should return "Did you get the cake?" and "It is dinner time !"
        assertEquals(2, Message.sentMessages.size());
        assertEquals("Did you get the cake?", Message.sentMessages.get(0).getMessageText());
        assertEquals("It is dinner time !", Message.sentMessages.get(1).getMessageText());
    }

    @Test
    public void testDisplayLongestMessage() {
        populateTestData();
        // Message 2 is the longest stored message
        String expectedLongest = "Where are you? You are late! I have asked you to be on time.";
        assertEquals(expectedLongest, Message.getLongestStoredMessage());
    }

    @Test
    public void testSearchForMessageID() {
        populateTestData();
        // Searching for Message 4's ID "0838884567"
        String expectedResult = "Recipient: 0838884567\nMessage: It is dinner time !";
        assertEquals(expectedResult, Message.searchMessageByID("0838884567"));
    }

    @Test
    public void testSearchMessagesByRecipient() {
        populateTestData();
        // Searching for +27838884567 (This is Message 2 and Message 5)
        String expectedResult = "Where are you? You are late! I have asked you to be on time.\nOk, I am leaving without you.";
        assertEquals(expectedResult, Message.searchMessagesByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageByHash() {
        populateTestData();
        
        // Get the hash of Message 2 (the first stored message)
        String hashToDelete = Message.storedMessages.get(0).getMessageHash();
        String expectedMessage = "Where are you? You are late! I have asked you to be on time.";
        
        // Delete it
        String result = Message.deleteMessageByHash(hashToDelete);
        
        // Verify the message says it was successfully deleted
        assertEquals("Message: \"" + expectedMessage + "\" successfully deleted.", result);
        
        // Verify it is no longer in the array
        assertEquals(1, Message.storedMessages.size());
    }

    @Test
    public void testDisplayReport() {
        populateTestData();
        
        // The report should contain the hash, recipient, and message for Sent messages
        String report = Message.generateReport();
        
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Recipient: 0838884567"));
        assertTrue(report.contains("Message: It is dinner time !"));
    }
}