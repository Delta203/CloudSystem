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
        String name = message.get("data").getAsJsonObject().get("name").getAsString();
        channel.initialise(name);
        break;
      case INGAME:
        System.out.println(MessageType.INGAME);
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
