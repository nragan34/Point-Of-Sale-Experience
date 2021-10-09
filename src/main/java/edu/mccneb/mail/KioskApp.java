package edu.mccneb.mail;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *  Main Java Class
 * **/
public class KioskApp {

  /////////////////////////////////////
  //// Welcome Message
  public static String welcome = "\nWelcome to the KIOSK";
  public static String lineSeparator = "---------------------------------------";

  /////////////////////////////////////
  //// Text Colors
  public static final String GREEN_BOLD = "\033[1;32m";
  // Reset
  public static final String RESET = "\033[0m";  // Text Reset


  /////////////////////////////////////
  //// Instantiate Classes
  CLI cli = new CLI();
  Tracks tracks = new Tracks();



  public static void main(String... args) {
    try {
      // create Usable Object to run this app
      KioskApp run = new KioskApp();
      // run app
      run.app();
      // terminate application
      System.out.println("\n\nGoodbye!");
    } catch (Exception e) {
      // catch exception
      e.printStackTrace();
    }
  }


  // Main app logic
  public void app() {


    System.out.println(welcome);
    System.out.println(lineSeparator);


    try {
      /////////////////////////////////////
      //// Connect To Database
      String jdbcURL = "jdbc:sqlite:/C:\\sqlite3\\databases\\chinook.db";
      Connection connection = DriverManager.getConnection(jdbcURL);

      /////////////////////////////////////
      //// Query All Tracks and Iterate/Print results
      ResultSet result = tracks.getAllTracks(connection);
      if (result != null) {
        List<String> allTracks = tracks.trackTableResultSet(result);
        iterateArrayList(allTracks);
      } else {
        System.out.println("Error! No results were returned from the tracks database table... ");
      }


      /////////////////////////////////////
      //// Query Single Track and Add To Playlist
      Boolean addAnotherTrack = true;
      List<String> addTrackList = new ArrayList<>();
      while (addAnotherTrack) {
        // select track id to add to playlist
        System.out.println("\n" + lineSeparator);
        // request input from user
        String userInput = cli.userInputString(GREEN_BOLD + "\nSelect a track by ID to add to your playlist: " + RESET);
        // query table for single track
        ResultSet singleTrack = tracks.getSingleTrack(connection, lineSeparator, userInput);
        // format result into string
        List<String> trackList = tracks.trackTableResultSet(singleTrack);
        // print result
        iterateArrayList(trackList);
      }

    } catch (SQLException e) {
      System.out.println("Error SQL exception while getting single track... ");
      e.printStackTrace();
    }

  }

  // iterate List<String> array
  public void iterateArrayList(List<String> list) {
    list
        .stream()
        .forEach(System.out::println);
  }


}
