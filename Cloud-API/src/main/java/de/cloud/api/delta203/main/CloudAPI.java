package de.cloud.api.delta203.main;

import de.cloud.api.delta203.core.Channel;

public class CloudAPI {

  public static void main(String[] args) {
    System.out.println("Simulate server...");
    Channel channel = new Channel("localhost", 1550, "key");
    channel.connect("Bedwars-1", 5000);
  }
}
