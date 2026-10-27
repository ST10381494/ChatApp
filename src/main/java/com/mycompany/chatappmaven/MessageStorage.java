package com.mycompany.chatappmaven;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MessageStorage {

    private static final String FILE_PATH = "messages.json";
    // I researched Gson to handle JSON files.
    // Reference: Google, "Gson User Guide", 2024.
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Saves a new message to the JSON file
    public void saveMessage(Message msg) {
        ArrayList<Message> currentList = loadStoredMessages();
        currentList.add(msg);
        writeToFile(currentList);
    }

    // Rewrites the entire file (used when deleting a message)
    public void rewriteStoredMessages() {
        writeToFile(Message.storedMessages);
    }

    // Reads the JSON file and returns the list of messages
    public ArrayList<Message> loadStoredMessages() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
            ArrayList<Message> messages = gson.fromJson(reader, listType);
            return messages != null ? messages : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    // Helper method to write the list to the file
    private void writeToFile(ArrayList<Message> list) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }
}