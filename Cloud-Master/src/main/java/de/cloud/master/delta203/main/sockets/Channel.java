package de.cloud.master.delta203.main.sockets;

import de.cloud.master.delta203.core.handlers.Communication;
import de.cloud.master.delta203.core.utils.ServerState;
import de.cloud.master.delta203.main.Cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Channel extends Thread {

  private final Socket socket;
  private final BufferedReader reader;
  private final Communication communication;

  private ServerState state;
  private String name;
  private int port;

  public Channel(Socket socket) throws IOException {
    this.socket = socket;
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    communication = new Communication(this);
  }

  public ServerState getChannelState() {
    return state;
  }

  public String getChannelName() {
    return name;
  }

  public int getChannelPort() {
    return port;
  }

  public void initialise(String name, int port) {
    state = ServerState.LOBBY;
    this.name = name;
    this.port = port;
    Cloud.server.getChannels().add(this);
    Cloud.console.print(this.name + ":" + this.port + " successfully connected.", "§bChannel§r");
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
        communication.handle(message);
      } catch (IOException e) {
        // happens when reader can not read anything <=> socket disconnected
        Cloud.server.getChannels().remove(this);
        break;
      }
    }
    Cloud.console.print(name + ":" + port + " has disconnected.", "§bChannel§r");
  }
}
