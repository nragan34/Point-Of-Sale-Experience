package edu.mccneb.mail;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Author - Nick Ragan
 **/
public class KioskApp {

  /////////////////////////////////////
  //// Welcome message
  public static String welcome = "\nWelcome to the KIOSK";
  public static String lineSeparator = "---------------------------------------";

  /////////////////////////////////////
  //// Text colors
  public static final String GREEN_BOLD = "\033[1;32m";
  public static final String WHITE_BOLD = "\033[1;37m";
  public static final String WHITE_UNDERLINE = "\033[4;37m";
  public static final String GREEN_UNDERLINE = "\033[4;32m";
  public static final String YELLOW_BOLD = "\033[1;33m";
  public static final String RED = "\033[0;31m";
  // Reset
  public static final String RESET = "\033[0m";  // Text Reset


  /////////////////////////////////////
  //// Instantiate classes
  CLI cli = new CLI();


  /////////////////////////////////////
  //// Class Level Lists / Maps
  List<Track> playList = new ArrayList<>();


  public static void main(String... args) {
    try {
      // create Usable Object to run this app
      KioskApp run = new KioskApp();

      // run app
      run.app();

      // terminate application
      System.out.println("\nGoodbye!");
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
      //// Connect to database
      String jdbcURL = "jdbc:sqlite:/C:\\sqlite3\\databases\\chinook.db";
      Connection connection = DriverManager.getConnection(jdbcURL);

      /////////////////////////////////////
      //// Query all tracks and print results
      ResultSet result = getAllTracks(connection);
      if (result != null) {
        stringReplTrack(result);
      } else {
        System.out.println(
            RED + "Error! No results were returned from the tracks database table... \n" + RESET);
      }

      /////////////////////////////////////
      //// Query single track and add to playlist
      Boolean addAnotherTrack = true;
      // User Playlist
      List<Track> playList = new ArrayList<>();
      while (addAnotherTrack) {

        // request input from user
        String userInput = cli.userInputString(
            GREEN_BOLD + "Select a track by Track ID to add it to your playlist: " + RESET);
        System.out.println(" ");

        // query table for single track
        ResultSet singleTrack = getSingleTrack(connection, lineSeparator, userInput);

        // For each item in trackList
        if (singleTrack != null) {

          // format result into string
          System.out.println(
              WHITE_UNDERLINE + "We are adding the following track to your playlist:" + RESET);

          // add track to playlist
          addTrackToPlaylist(singleTrack);

          // Continue adding tracks (y/n)
          addAnotherTrack = addAnotherTrack();

        } else {
          System.out.println(RED + "Your selection was not valid. Please try again!" + RESET);
          System.out.println(" ");
        }
      }


      createInvoice();
      viewPlaylist();

    } catch (SQLException e) {
      System.out.println(RED + "Error SQL exception while getting single track... \n" + RESET);
      e.printStackTrace();
    }
  }


  // prompt user to add more tracks
  public Boolean addAnotherTrack() {
    while (true) {

      // add another track
      String addAnotherTrack = cli.userInputString(
          "Would you like to add another track? (y/n) ");
      System.out.println(" ");

      if (addAnotherTrack.equalsIgnoreCase("y")) {
        return true;
      } else if (addAnotherTrack.equalsIgnoreCase("n")) {
        return false;
      } else {
        System.out.println(RED + "Error! Please select either (y/n).\n" + RESET);
        continue;
      }
    }
  }



  // return all results from tracks table
  public ResultSet getAllTracks(Connection connection) {
    try {
      // query everything from the tracks table
      PreparedStatement sql = connection.prepareStatement("SELECT * FROM tracks");
      ResultSet result = sql.executeQuery();
      return result;
    } catch (SQLException e) {
      System.out.println("Error selecting all from tracks... ");
    }
    return null;
  }


  // return a single track from tracks table
  public ResultSet getSingleTrack(Connection connection, String lineSeparator, String userInput) {
    try {
      // Select track by id
      Integer userInputInt = Integer.parseInt(userInput);
      // Query Statement
      PreparedStatement sql = connection.prepareStatement("SELECT * FROM tracks where trackid = ?");
      // Set track id
      sql.setInt(1, userInputInt);
      // get result of query
      ResultSet result = sql.executeQuery();
      return result;
    } catch (SQLException e) {
      System.out.println("Error selecting all from tracks... ");
    } catch (NumberFormatException e) {
      System.out.println("Error! Your entry must be an integer!");
      return null;
    }
    return null;
  }


  // loop through tracks table results and print
  public void stringReplTrack(ResultSet result) {
    try {
      while (result.next()) {
        String trackid = result.getString("trackid");
        String name = result.getString("name");
        String composer = result.getString("composer");
        String unitPrice = result.getString("unitprice");

        String concatString =
            WHITE_BOLD + "Track ID:" + RESET + " " + trackid + WHITE_BOLD + "\nName: " + RESET
                + name + WHITE_BOLD + "\nComposer: " + RESET + composer + WHITE_BOLD + "\nPrice: "
                + RESET + unitPrice;

        // String Representation
        System.out.println(concatString + "\n");
      }
    } catch (SQLException e) {
      System.out.println("Error looping through result set.... ");
      e.printStackTrace();
    }
  }


  // Add track to playlist
  public void addTrackToPlaylist(ResultSet singleTrack) {
    try{
      while(singleTrack.next()) {
        // Create Track Object
        int trackid = singleTrack.getInt("trackId");
        String name = singleTrack.getString("name");
        String composer = singleTrack.getString("composer");
        double unitPrice = singleTrack.getDouble("unitprice");

        if (!checkDuplicateTracks(trackid)) {
          Track newTrack = new Track(trackid, name, composer, unitPrice);

          System.out.println(WHITE_BOLD + "Track ID:" + RESET + " " + trackid + WHITE_BOLD + "\nName: " + RESET
              + name + WHITE_BOLD + "\nComposer: " + RESET + composer + WHITE_BOLD + "\nPrice: "
              + RESET + unitPrice);

          System.out.println(" ");

          playList.add(newTrack);
        } else {
          System.out.println("\nYou already have that track in your playlist! \n");
        }

      }
    }catch(SQLException e) {
      System.out.println("Error");
    }
  }

  // check tracks to see if track exists
  public Boolean checkDuplicateTracks(int trackid) {
      for (int i=0; i<playList.size(); i++ ) {
        if (playList.get(i).getTrackId() == trackid) {
          return true;
        }
      }
      return false;
  }

  // create invoice
  public void createInvoice() {
    double totalPrice = 0;
    for (Track tracks : playList) {
      totalPrice += tracks.getUnitPrice();
    }
    System.out.println(WHITE_UNDERLINE + "- - - - - - - - - - - - - - - - - -" + RESET);
    totalPrice = Math.round(totalPrice * 100) / 100.0 ;
    System.out.println(GREEN_BOLD + "\nThe total cost for your playlist is " + RESET + "$" + totalPrice);
  }

  // show playlist
  public void viewPlaylist() {
    System.out.println(WHITE_BOLD + "\nYour Playlist:" + RESET);
    for(Track track : playList) {
      System.out.println(track.getTrackId() + " " + track.getName() + " " + track.getUnitPrice());
    }
    System.out.println("\n" + WHITE_UNDERLINE + "- - - - - - - - - - - - - - - - - -" + RESET);
  }

}
