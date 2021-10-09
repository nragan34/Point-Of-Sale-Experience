package edu.mccneb.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI {


  ////////////////////////////
  // Get user input as a string
  public String userInputString(String msg) {
    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
    System.out.print(msg);
    try {
      String getUserInput = userInput.readLine();
      return getUserInput;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }


}
