package de.cloud.master.delta203.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class KeyGenerator {

  private String key;

  public KeyGenerator() {
    key = null;
  }

  private String getDate() {
    return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
  }

  private int getRandom() {
    return new Random().nextInt(1000000);
  }

  private String md5(String string) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("sha256");
      byte[] bytesHash = messageDigest.digest(string.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte b : bytesHash) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public void generate() {
    String raw = getRandom() + getDate() + getRandom();
    key = md5(raw);
  }

  public String getKey() {
    return key;
  }
}
