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

package de.cloud.master.delta203.core.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.core.utils.MessageType;
import de.cloud.master.delta203.main.Cloud;
import de.cloud.master.delta203.main.sockets.Channel;

public class Communication {

  public Communication() {}

  private boolean isEmpty(String string) {
    return (string == null || string.isEmpty());
  }

  private boolean notJson(String string) {
    try {
      JsonParser.parseString(string).getAsJsonObject();
      return false;
    } catch (Exception ignored) {
      return true;
    }
  }

  public boolean isValid(String key, String string) {
    if (isEmpty(string)) return false;
    if (notJson(string)) return false;
    JsonObject message = JsonParser.parseString(string).getAsJsonObject();
    if (!message.has("key") || !message.has("type") || !message.has("data")) return false;
    return message.get("key").getAsString().equals(key);
  }

  public void handle(Channel channel, String string) {
    JsonObject message = JsonParser.parseString(string).getAsJsonObject();
    switch (MessageType.valueOf(message.get("type").getAsString())) {
      case CONNECT:
        String name = message.get("data").getAsJsonObject().get("service").getAsString();
        channel.initialise(name);
        break;
      case INGAME:
        Service service = channel.getService();
        service.setServiceInGame();
        break;
    }
  }

  public void broadcastProxies(String message) {
    for (Service service : Cloud.services.values()) {
      if (service.getServiceGroup().getType() == GroupType.SERVER) continue;
      Channel channel = service.getServiceChannel();
      channel.sendMessage(message);
    }
  }

  /*
   * Message builders
   */

  public JsonObject addServerMessage(String name, int port) {
    JsonObject message = new JsonObject();
    message.addProperty("key", Cloud.key);
    message.addProperty("type", MessageType.ADDSERVER.name());
    JsonObject data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("ip", Cloud.server.getIp());
    data.addProperty("port", port);
    message.add("data", data);
    return message;
  }

  public JsonObject removeServerMessage(String name) {
    JsonObject message = new JsonObject();
    message.addProperty("key", Cloud.key);
    message.addProperty("type", MessageType.REMOVESERVER.name());
    JsonObject data = new JsonObject();
    data.addProperty("name", name);
    message.add("data", data);
    return message;
  }

  public JsonObject dispatchCommandMessage(String command) {
    JsonObject message = new JsonObject();
    message.addProperty("key", Cloud.key);
    message.addProperty("type", MessageType.COMMAND.name());
    JsonObject data = new JsonObject();
    data.addProperty("command", command);
    message.add("data", data);
    return message;
  }
}
