package de.cloud.master.delta203.main.sockets;

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.core.handlers.Communication;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.core.utils.ServerState;
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

  private ServerState state;
  private String name;
  private int port;
  private Service service;

  public Channel(Socket socket) throws IOException {
    this.socket = socket;
    writer = new PrintWriter(socket.getOutputStream());
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

  public GroupType getGroupType() {
    return service.getServiceGroup().getType();
  }

  public void initialise(String name, int port) {
    state = ServerState.LOBBY;
    this.name = name;
    this.port = port;
    service = Cloud.services.get(name);
    Cloud.server.getChannels().add(this);
    Cloud.console.print(this.name + ":" + this.port + " successfully connected.", "§bChannel§r");
    if (getGroupType() == GroupType.PROXY) {
      // broadcast online channels to new proxy
      for (Channel channel : Cloud.server.getChannels()) {
        if (channel.service.getServiceGroup().getType() != GroupType.SERVER) continue;
        communication.broadcastProxies(
            communication.addServerMessage(channel.name, channel.port).toString());
      }
    } else {
      // broadcast new service to proxy
      communication.broadcastProxies(communication.addServerMessage(name, port).toString());
    }
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
        communication.handle(message);
      } catch (IOException e) {
        // happens when reader can not read anything <=> socket disconnected
        Cloud.server.getChannels().remove(this);
        break;
      }
    }
    Cloud.console.print(name + ":" + port + " has disconnected.", "§bChannel§r");
    communication.broadcastProxies(communication.removeServerMessage(name).toString());
    service.stopProcess();
  }
}
