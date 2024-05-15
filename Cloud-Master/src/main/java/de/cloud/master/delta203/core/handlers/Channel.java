package de.cloud.master.delta203.core.handlers;

import de.cloud.master.delta203.main.Cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Channel extends Thread {

  private final Socket socket;
  private final BufferedReader reader;

  private String server;

  public Channel(Socket socket) throws IOException {
    this.socket = socket;
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public Socket getSocket() {
    return socket;
  }

  public String getServer() {
    return server;
  }

  public void close() {
    try {
      socket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    try {
      String message = reader.readLine();
      server = message;
      Cloud.console.print(server + " connected to the cloud.");

      while (!Cloud.shutdown) {
        message = reader.readLine();
        if (message != null) Cloud.console.print(message);
      }
    } catch (IOException ignored) {
      Cloud.server.removeChannel(this);
      Cloud.console.print(server + " disconnected.");
    }
  }
}
