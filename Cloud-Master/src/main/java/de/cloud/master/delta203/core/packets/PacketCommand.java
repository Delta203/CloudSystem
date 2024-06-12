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

/** This class is a cloud packet. It is sent to a server socket to execute a command. */
public class PacketCommand {

  public static final MessageType type = MessageType.COMMAND;

  private String command;

  public PacketCommand() {}

  public String getCommand() {
    return command;
  }

  public JsonObject getAsJson() {
    JsonObject message = new JsonObject();
    message.addProperty("key", Cloud.key);
    message.addProperty("type", type.name());
    JsonObject data = new JsonObject();
    data.addProperty("command", command);
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
   * This method adds a command to the packet.
   *
   * @param command the command to be sent
   */
  public void c(String command) {
    this.command = command;
  }
}
