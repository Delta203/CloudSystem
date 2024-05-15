package de.cloud.master.delta203.main.sockets;

import de.cloud.master.delta203.core.handlers.Channel;
import de.cloud.master.delta203.main.Cloud;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

  private final String ip;
  private final int port;

  private final ServerSocket server;
  private final List<Channel> channels;

  public Server() throws IOException {
    ip = Cloud.config.getData().get("ip").getAsString();
    port = Cloud.config.getData().get("port").getAsInt();
    server = new ServerSocket(port);
    channels = new ArrayList<>();
    Cloud.console.print("Server socket listening to: " + ip + ":" + port);
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  public List<Channel> getChannels() {
    return channels;
  }

  public void removeChannel(Channel channel) {
    channels.remove(channel);
  }

  public void close() {
    for (Channel channel : channels) {
      channel.close();
    }
    try {
      server.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    while (!Cloud.shutdown) {
      try {
        Socket socket = server.accept();
        Channel channel = new Channel(socket);
        channels.add(channel);
        channel.start();
      } catch (IOException ignored) {
      }
    }
    Cloud.console.print("The server socket is closed.");
  }
}
