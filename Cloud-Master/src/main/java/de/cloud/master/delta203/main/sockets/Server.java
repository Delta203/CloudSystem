/*
 * Copyright 2024 Cloud System by Delta203
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
