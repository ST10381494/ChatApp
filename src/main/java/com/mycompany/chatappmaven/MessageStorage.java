package com.mycompany.chatappmaven;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;

public class MessageStorage {

    // I researched how to save a file using Gson library.
    // Reference: Google, "Gson User Guide", 2024.
    public void saveMessage(Message msg) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        try {
            // open the file to write (append)
            FileWriter writer = new FileWriter("messages.json", true);
            
            // convert message object to JSON and write
            gson.toJson(msg, writer);
            writer.write("\n"); // add a new line for the next message
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}