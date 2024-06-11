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

package de.cloud.api.delta203.core;

import de.cloud.api.delta203.core.handlers.CloudCommunication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CloudChannel extends Thread {

  private final String name;
  private final String serverIp;
  private final int serverPort;
  private final String serverKey;

  private final CloudCommunication communication;

  private Socket socket;
  private PrintWriter writer;
  private BufferedReader reader;

  public CloudChannel(String name, String serverIp, int serverPort, String serverKey) {
    this.name = name;
    this.serverIp = serverIp;
    this.serverPort = serverPort;
    this.serverKey = serverKey;
    communication = new CloudCommunication();
  }

  /**
   * This method connects the channel to the server socket.
   *
   * @return if the channel is connected
   */
  public boolean connect() {
    System.out.println("Channel " + name + " connecting to:");
    System.out.println("Address: " + serverIp + ":" + serverPort);
    System.out.println("Key: " + serverKey);
    try {
      socket = new Socket(serverIp, serverPort);
      writer = new PrintWriter(socket.getOutputStream());
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      System.out.println("Channel successfully connected!");
      return true;
    } catch (IOException ignored) {
      // server is not accessible
      System.out.println("Channel can not connect to cloud server!");
    }
    return false;
  }

  /**
   * This method sends a message to the server socket.
   *
   * @param string the message to be sent
   */
  public void sendMessage(String string) {
    writer.println(string);
    writer.flush();
  }

  @Override
  public void run() {
    // run main loop
    while (socket.isConnected()) {
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
