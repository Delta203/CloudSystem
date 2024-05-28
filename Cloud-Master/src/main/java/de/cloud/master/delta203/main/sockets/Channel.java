package de.cloud.master.delta203.main.sockets;

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.core.handlers.Communication;
import de.cloud.master.delta203.main.Cloud;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Channel extends Thread {

  private final Socket socket;
  private final PrintWriter writer;
  private final BufferedReader reader;

  private final Communication communication;
  private Service service;

  public Channel(Socket socket) throws IOException {
    this.socket = socket;
    writer = new PrintWriter(socket.getOutputStream());
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    communication = new Communication();
  }

  public Communication getCommunication() {
    return communication;
  }

  public void initialise(String name) {
    service = Cloud.services.get(name);
    service.registerChannel(this);
  }

  /**
   * This method closes the channel. However, it is only called when the server is closed. As a
   * result, the extra removal from the channel list is no longer necessary.
   */
  public void close() {
    try {
      socket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendMessage(String string) {
    writer.println(string);
    writer.flush();
  }

  @Override
  public void run() {
    // run main loop
    while (!Cloud.shutdown) {
      try {
        // read message
        String message = reader.readLine();
        // check message
        if (!communication.isValid(Cloud.key, message)) {
          // invalid -> close socket and stop loop
          socket.close();
          return;
        }
        // valid -> handle message
        communication.handle(this, message);
      } catch (IOException e) {
        // happens when reader can not read anything <=> socket disconnected
        break;
      }
    }
    communication.broadcastProxies(
        communication.removeServerMessage(service.getServiceName()).toString());
    service.stopProcess();
  }
}
