package com.mycompany.chatappmaven;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @BeforeEach
    public void reset() {
        // Reset the static variables before each test so they don't interfere
        Message.totalMessagesSent = 0;
        Message.sentMessages.clear();
    }

    @Test
    public void testMessageLengthValid() {
        Message msg = new Message(0);
        msg.setMessageText("Short message");
        assertTrue(msg.getMessageText().length() <= 250);
    }

    @Test
    public void testMessageLengthInvalid() {
        Message msg = new Message(0);
        String longText = "";
        for (int i = 0; i < 260; i++) {
            longText += "a";
        }
        msg.setMessageText(longText);
        // It should fail the 250 limit
        assertFalse(msg.getMessageText().length() <= 250);
    }

    @Test
    public void testRecipientValid() {
        Message msg = new Message(0);
        msg.setRecipient("+27718693002");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testRecipientInvalid() {
        Message msg = new Message(0);
        msg.setRecipient("08575975889");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", msg.checkRecipientCell());
    }

    @Test
    public void testMessageHash() {
        // Message 0 will automatically generate an ID starting with "00"
        Message msg = new Message(0);
        msg.setRecipient("+27718693002");
        msg.setMessageText("Hi Mike, can you join us for dinner tonight?");
        
        String hash = msg.createMessageHash();
        
        // Check that the hash is exactly 00:0:HITONIGHT
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testSendMessage() {
        Message msg = new Message(0);
        String result = msg.SentMessage("1");
        
        assertEquals("Message successfully sent.", result);
        assertEquals(1, Message.returnTotalMessages());
        assertEquals("Sent", msg.getFlag());
    }
    
    @Test
    public void testDisregardMessage() {
        Message msg = new Message(1);
        String result = msg.SentMessage("3");
        
        assertEquals("Message disregarded.", result);
        assertEquals(0, Message.returnTotalMessages()); // Total sent should still be 0
        assertEquals("Disregard", msg.getFlag());
    }
}