package edu.mccneb.mail;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Author - Nick Ragan
 **/
public class KioskApp {

  /////////////////////////////////////
  //// Welcome Message
  public static String welcome = "\nWelcome to the KIOSK";
  public static String lineSeparator = "---------------------------------------";

  /////////////////////////////////////
  //// Text Colors
  public static final String GREEN_BOLD = "\033[1;32m";
  public static final String WHITE_BOLD = "\033[1;37m";
  public static final String WHITE_UNDERLINE = "\033[4;37m";
  public static final String GREEN_UNDERLINE = "\033[4;32m";
  public static final String YELLOW_BOLD = "\033[1;33m";
  public static final String RED = "\033[0;31m";
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
        System.out.println(
            RED + "\nError! No results were returned from the tracks database table... \n" + RESET);
      }



      /////////////////////////////////////
      //// Query Single Track and Add To Playlist
      Boolean addAnotherTrack = true;
      // User Playlist
      List<String> playList = new ArrayList<>();
      while (addAnotherTrack) {

        // request input from user
        String userInput = cli.userInputString(
            GREEN_BOLD + "\nSelect a track by Track ID to add it to your playlist: " + RESET);

        // query table for single track
        ResultSet singleTrack = tracks.getSingleTrack(connection, lineSeparator, userInput);

        if (singleTrack != null) {
          // format result into string
          List<String> trackList = tracks.trackTableResultSet(singleTrack);
          // For each item in trackList
          trackList.stream().forEach(e -> {
            System.out.println(WHITE_UNDERLINE + "ADDING TO YOUR PLAYLIST" + RESET + ":\n");
            playList.add(e);
            iterateArrayList(trackList);
            System.out.println(WHITE_UNDERLINE + "                    " + RESET);
          });
          // Continue adding tracks (y/n)
          addAnotherTrack = addAnotherTrack();
        } else {
          System.out.println(RED + "Your selection was not valid. Please try again!" + RESET);
        }


      }

      /////////////////////////////////////
      //// Show Invoice For PlayList

      // for everything in playlist list create a track object
      playList
          .stream()
          .forEach(e -> {

          });

      // create an invoice_item for everything in playlist

      // create one invoice for all invoice items





    } catch (SQLException e) {
      System.out.println(RED + "\nError SQL exception while getting single track... \n" + RESET);
      e.printStackTrace();
    }
  }


  // prompt user to add more tracks
  public Boolean addAnotherTrack() {

    while (true) {
      // add another track
      String addAnotherTrack = cli.userInputString(
          "\n\nWould you like to add another track? (y/n) ");

      if (addAnotherTrack.equalsIgnoreCase("y")) {
        return true;
      } else if (addAnotherTrack.equalsIgnoreCase("n")) {
        return false;
      } else {
        System.out.println(RED + "\nError! Please select either (y/n).\n" + RESET);
        continue;
      }
    }
  }

  // iterate List<String> array
  public void iterateArrayList(List<String> list) {
    list
        .stream()
        .forEach(e -> {
          System.out.print(e);
          System.out.println("\n ");
        });
  }


}
