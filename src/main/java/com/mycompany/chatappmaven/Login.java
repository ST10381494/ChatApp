package com.mycompany.chatappmaven;

public class Login {

    // variables to store the user details
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellNumber;

    // Method 1: check username has underscore and is <= 5 chars
    public boolean checkUserName(String user) {
        if (user.contains("_") && user.length() <= 5) {
            return true;
        } else {
            return false;
        }
    }

    // Method 2: check password rules (8 chars, capital, number, special)
    public boolean checkPasswordComplexity(String pass) {
        if (pass.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        if (hasCapital && hasNumber && hasSpecial) {
            return true;
        } else {
            return false;
        }
    }

    // Method 3: cell phone must start with + and have international code
    public boolean checkCellPhoneNumber(String cell) {
        // I researched that matches() can check for a '+' and then digits
        String regex = "^\\+[0-9]{9,11}$";
        if (cell.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    // Method 4: register user and return messages
    public String registerUser(String user, String pass, String cell) {
        if (!checkUserName(user)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(pass)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cell)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // if everything is valid, save to variables
        this.username = user;
        this.password = pass;
        this.cellNumber = cell;
        
        return "User registered successfully.";
    }

    // Set the names when registering
    public void setName(String fName, String lName) {
        this.firstName = fName;
        this.lastName = lName;
    }

    // Method 5: login check
    public boolean loginUser(String user, String pass) {
        if (user.equals(this.username) && pass.equals(this.password)) {
            return true;
        } else {
            return false;
        }
    }

    // Method 6: return login status message
    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}