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
import de.cloud.master.delta203.core.handlers.Communication;
import de.cloud.master.delta203.core.packets.PacketRemoveServer;
import de.cloud.master.delta203.core.packets.PacketServiceInfo;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.main.Cloud;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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

  public Service getService() {
    return service;
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

  public void sendMessage(String message) {
    writer.println(message);
    writer.flush();
  }

  public void broadcast(String message, boolean onlyProxy) {
    for (Service service : Cloud.services.values()) {
      // skip unloaded services
      if (service.getServiceChannel() == null) continue;
      // only proxy broadcast
      if (onlyProxy && service.getServiceGroup().getType() == GroupType.SERVER) continue;
      Channel channel = service.getServiceChannel();
      channel.sendMessage(message);
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
          break;
        }
        // valid -> handle message
        communication.handle(this, message);
      } catch (IOException e) {
        // happens when reader can not read anything <=> socket disconnected
        break;
      }
    }

    Cloud.console.print(
        service.getServiceName() + ":" + service.getServicePort() + " has disconnected.",
        "§cChannel§r");
    // send remove server
    PacketRemoveServer removeServer = new PacketRemoveServer();
    removeServer.n(service.getServiceName());
    broadcast(removeServer.message(), true);
    // broadcast service infos
    PacketServiceInfo serviceInfo = new PacketServiceInfo();
    serviceInfo.s(new ArrayList<>(Cloud.services.values()));
    broadcast(serviceInfo.message(), false);
    // stop service
    service.stopProcess(true);
  }
}
