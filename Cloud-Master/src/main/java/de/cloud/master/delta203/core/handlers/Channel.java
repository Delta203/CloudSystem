package de.cloud.master.delta203.core.handlers;

import de.cloud.master.delta203.main.Cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Channel extends Thread {

  private final Socket socket;
  private final BufferedReader reader;

  private String key;
  private String name;
  private int port;

  public Channel(Socket socket) throws IOException {
    this.socket = socket;
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public Socket getSocket() {
    return socket;
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
      name = message;
      Cloud.console.print(name + " connected to cloud.");

      while (!Cloud.shutdown) {
        message = reader.readLine();
        if (message != null) Cloud.console.print(message);
      }
    } catch (IOException ignored) {
      Cloud.server.removeChannel(this);
      Cloud.console.print(name + " disconnected from cloud.");
    }
  }
}
