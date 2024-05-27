package de.cloud.api.delta203.core;

import de.cloud.api.delta203.core.handlers.Communication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Channel extends Thread {

  private final String serverIp;
  private final int serverPort;
  private final String serverKey;
  private final Communication communication;

  private Socket socket;
  private PrintWriter writer;
  private BufferedReader reader;

  public Channel(String serverIp, int serverPort, String serverKey) {
    this.serverIp = serverIp;
    this.serverPort = serverPort;
    this.serverKey = serverKey;
    communication = new Communication(serverKey);
  }

  public Socket getSocket() {
    return socket;
  }

  private void sendMessage(String string) {
    writer.println(string);
    writer.flush();
  }

  public void connect(String name) {
    System.out.println(
        "Channel connecting to "
            + serverIp
            + ":"
            + serverPort
            + " (Name: "
            + name
            + ", Key: "
            + serverKey
            + ")");
    try {
      socket = new Socket(serverIp, serverPort);
      writer = new PrintWriter(socket.getOutputStream());
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      sendMessage(communication.connectMessage(name).toString());
      start();
    } catch (IOException ignored) {
      // server is not accessible
      System.out.println("Channel can not connect to cloud server!");
    }
  }

  @Override
  public void run() {
    // run main loop
    while (true) {
      try {
        // read message
        String message = reader.readLine();
        // check message
        if (message == null || message.isEmpty()) {
          // server disconnected
          System.out.println("Cloud server socket disconnected!");
          return;
        }
        if (communication.isValid(serverKey, message)) {
          // valid -> handle message
          communication.handle(message);
        }
      } catch (IOException e) {
        // happens when reader can not read anything <=> socket disconnected
        System.out.println("Cloud server socket disconnected!");
        break;
      }
    }
  }
}
