package de.cloud.master.delta203.main.sockets;

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.main.Cloud;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

  private final String ip;
  private final int port;

  private final ServerSocket server;

  public Server() throws IOException {
    ip = Cloud.config.getData().get("ip").getAsString();
    port = Cloud.config.getData().get("port").getAsInt();
    server = new ServerSocket(port);
    Cloud.console.print("Server socket listening to: " + ip + ":" + port);
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  public void close() {
    // close channels (sockets)
    for (Service service : Cloud.services.values()) {
      if (service.getServiceChannel() == null) continue;
      service.getServiceChannel().close();
    }
    // close server socket
    try {
      server.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Cloud.console.print("The server socket is closed.");
  }

  @Override
  public void run() {
    // run main loop
    while (!Cloud.shutdown) {
      try {
        // allow new sockets to connect and open a new channel
        Socket socket = server.accept();
        Channel channel = new Channel(socket);
        channel.start();
      } catch (IOException ignored) {
      }
    }
  }
}
