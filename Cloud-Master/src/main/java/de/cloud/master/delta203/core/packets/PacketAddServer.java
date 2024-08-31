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

package de.cloud.master.delta203.core.packets;

import com.google.gson.JsonObject;
import de.cloud.master.delta203.core.utils.MessageType;
import de.cloud.master.delta203.main.Cloud;

/** This class is a cloud packet. It is sent to the proxy socket to add a server. */
public class PacketAddServer {

  public static final MessageType type = MessageType.ADDSERVER;

  private String name;
  private String ip;
  private int port;

  public PacketAddServer() {}

  public String getName() {
    return name;
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  public JsonObject getAsJson() {
    JsonObject message = new JsonObject();
    message.addProperty("key", Cloud.key);
    message.addProperty("type", type.name());
    JsonObject data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("ip", ip);
    data.addProperty("port", port);
    message.add("data", data);
    return message;
  }

  /**
   * This method gets the full packet message from {@link JsonObject}.
   *
   * @return the packet message
   */
  public String message() {
    return getAsJson().toString();
  }

  /**
   * This method adds a name to the packet.
   *
   * @param name the service name
   */
  public void n(String name) {
    this.name = name;
  }

  /**
   * This method adds an ip to the packet.
   *
   * @param ip the service ip
   */
  public void i(String ip) {
    this.ip = ip;
  }

  /**
   * This method adds a port to the packet.
   *
   * @param port the service port
   */
  public void p(int port) {
    this.port = port;
  }
}
