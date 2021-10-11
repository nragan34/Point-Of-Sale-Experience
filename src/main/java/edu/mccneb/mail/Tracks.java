package edu.mccneb.mail;

import static edu.mccneb.mail.KioskApp.RESET;
import static edu.mccneb.mail.KioskApp.WHITE_BOLD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tracks {

  private int trackId;
  private String name;
  private int albumId;
  private int mediaTypeId;
  private int genreId;
  private String composer;
  private int milliseconds;
  private int bytes;
  private double unitPrice;


  public  Tracks() {

  }

  public Tracks(int trackId, String name, int albumId, int mediaTypeId, int genreId,
      String composer, int milliseconds, int bytes, double unitPrice) {
    this.trackId = trackId;
    this.name = name;
    this.albumId = albumId;
    this.mediaTypeId = mediaTypeId;
    this.genreId = genreId;
    this.composer = composer;
    this.milliseconds = milliseconds;
    this.bytes = bytes;
    this.unitPrice = unitPrice;
  }

  public int getTrackId() {
    return trackId;
  }

  public void setTrackId(int trackId) {
    this.trackId = trackId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAlbumId() {
    return albumId;
  }

  public void setAlbumId(int albumId) {
    this.albumId = albumId;
  }

  public int getMediaTypeId() {
    return mediaTypeId;
  }

  public void setMediaTypeId(int mediaTypeId) {
    this.mediaTypeId = mediaTypeId;
  }

  public int getGenreId() {
    return genreId;
  }

  public void setGenreId(int genreId) {
    this.genreId = genreId;
  }

  public String getComposer() {
    return composer;
  }

  public void setComposer(String composer) {
    this.composer = composer;
  }

  public int getMilliseconds() {
    return milliseconds;
  }

  public void setMilliseconds(int milliseconds) {
    this.milliseconds = milliseconds;
  }

  public int getBytes() {
    return bytes;
  }

  public void setBytes(int bytes) {
    this.bytes = bytes;
  }

  public double getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(double unitPrice) {
    this.unitPrice = unitPrice;
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
      sql.setInt(1,userInputInt);
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
  public List<String> trackTableResultSet(ResultSet result) {

    List<String> trackTableResults = new ArrayList<>();

    try {
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

        String concatString = WHITE_BOLD + "Track ID:" + RESET + " " + trackid + WHITE_BOLD + "\nName: " + RESET + name + WHITE_BOLD +  "\nComposer: " + RESET + composer + WHITE_BOLD +  "\nPrice: " + RESET +  unitPrice;
        System.out.println("\n\n");

        trackTableResults.add(result);
      }
    } catch (SQLException e) {
      System.out.println("Error looping through result set.... ");
      e.printStackTrace();
      return null;
    }
    return trackTableResults;
  }
}
