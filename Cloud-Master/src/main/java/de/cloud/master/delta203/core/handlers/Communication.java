package de.cloud.master.delta203.core.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.cloud.master.delta203.core.utils.MessageType;
import de.cloud.master.delta203.main.Cloud;
import de.cloud.master.delta203.main.sockets.Channel;

public class Communication {

  private final Channel channel;

  public Communication(Channel channel) {
    this.channel = channel;
  }

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

  public void handle(String string) {
    JsonObject message = JsonParser.parseString(string).getAsJsonObject();
    switch (MessageType.valueOf(message.get("type").getAsString())) {
      case CONNECT:
        String name = message.get("data").getAsJsonObject().get("name").getAsString();
        int port = message.get("data").getAsJsonObject().get("port").getAsInt();
        channel.initialise(name, port);
        broadcast(addServerMessage(name, port).toString());
        break;
      case INGAME:
        System.out.println(MessageType.INGAME);
        break;
    }
  }

  public void broadcast(String message) {
    for (Channel channels : Cloud.server.getChannels()) {
      channels.sendMessage(message);
    }
  }

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
}
