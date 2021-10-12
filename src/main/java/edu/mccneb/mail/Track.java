package edu.mccneb.mail;

import static edu.mccneb.mail.KioskApp.RESET;
import static edu.mccneb.mail.KioskApp.WHITE_BOLD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Track {


  private int trackId;
  private String name;
  private String composer;
  private double unitPrice;


  public Track() {

  }

  public Track(int trackId, String name, String composer, double unitPrice) {
    this.trackId = trackId;
    this.name = name;
    this.composer = composer;
    this.unitPrice = unitPrice;
  }


  public int getTrackId() {
    return trackId;
  }

  public String getName() {
    return name;
  }

  public double getUnitPrice() {
    return unitPrice;
  }

}
