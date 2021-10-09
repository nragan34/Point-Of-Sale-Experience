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
  public static String lineSeparator = "--------------------";

  /////////////////////////////////////
  //// Instantiate Classes
  CLI cli = new CLI();



  public static void main(String... args) {

    try {
      // create Usable Object to run this app
      KioskApp run = new KioskApp();
      // run app
      run.app();
      // terminate application
      System.out.println("Goodbye!");
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
      // sqlite3 path
      String jdbcURL = "jdbc:sqlite:/C:\\sqlite3\\databases\\chinook.db";
      // create database connection
      Connection connection = DriverManager.getConnection(jdbcURL);

      // query everything from tracks
      ResultSet result = getAllTracks(connection);
      loopResultSet(result);

      // select track by id to add to playlist
      Boolean addAnotherTrack = true;
      List<String> addTrackList = new ArrayList<>();
      while (addAnotherTrack) {
        // method to add track
        ResultSet singleTrack = getSingleTrack(connection);

        loopResultSet(singleTrack);
        break;
      }

    } catch (SQLException e) {
      System.out.println("Error");
      e.printStackTrace();
    }

  }


  // select tracks and add them to playlist
  public String SelectAndAddTracks(String msg) {
    String userInput = cli.userInputString(msg);
    return userInput;
  }

// loop through tracks table results
  public void loopResultSet(ResultSet result) {
    try {
      result.next();
      while (result.next()) {
        String trackid = result.getString("trackid");
        String name = result.getString("name");
        String albumId = result.getString("Albumid");
        String mediaTypeId = result.getString("mediatypeid");
        String genreId = result.getString("genreid");
        String composer= result.getString("composer");
        String milliseconds = result.getString("milliseconds");
        String bytes = result.getString("bytes");
        String unitPrice = result.getString("unitprice");

        String concatString = trackid + " " + name + " " + albumId + " " + mediaTypeId + " " + genreId + " " + composer + " " + milliseconds + " " + bytes + " " +  unitPrice;

        System.out.println(concatString);
      }
    } catch (SQLException e) {
      System.out.println("Error looping through result set.... ");
      e.printStackTrace();
    }
  }


  // tracks table
  public ResultSet getAllTracks(Connection myconnection) {
    try {
      // query everything from the tracks table
      PreparedStatement sql = myconnection.prepareStatement("SELECT * FROM tracks");
      ResultSet result = sql.executeQuery();
      return result;
    } catch (SQLException e) {
      System.out.println("Error selecting all from tracks... ");
    }
    return null;
  }


  // tracks table
  public ResultSet getSingleTrack(Connection connection) {
    try {
      // select track id to add to playlist
      String userInput = SelectAndAddTracks("Select a track by ID to add to your playlist: ");
      // Select track by id
      Integer userInputInt = Integer.parseInt(userInput);
      // Query Statement
      System.out.println("Printing user input = " + userInputInt);
      PreparedStatement sql = connection.prepareStatement("SELECT * FROM tracks where trackid = ?");
      // Set track id
      sql.setInt(1,userInputInt);
      // get result of query
      ResultSet result = sql.executeQuery();
      System.out.println("Size of results = " + result.getFetchSize());
      // return result set
      return result;
    } catch (SQLException e) {
      System.out.println("Error selecting all from tracks... ");
    }
    return null;
  }


}
