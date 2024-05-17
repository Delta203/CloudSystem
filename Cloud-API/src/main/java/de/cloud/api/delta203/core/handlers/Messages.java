package de.cloud.api.delta203.core.handlers;

import com.google.gson.JsonObject;
import de.cloud.api.delta203.core.utils.MessageType;

public class Messages {

  private final String key;

  public Messages(String key) {
    this.key = key;
  }

  public JsonObject connectMessage(String name, int port) {
    JsonObject message = new JsonObject();
    message.addProperty("key", key);
    message.addProperty("type", MessageType.CONNECT.name());
    JsonObject data = new JsonObject();
    data.addProperty("name", name);
    data.addProperty("port", port);
    message.add("data", data);
    return message;
  }

  public JsonObject inGameMessage() {
    JsonObject message = new JsonObject();
    message.addProperty("key", key);
    message.addProperty("type", MessageType.INGAME.name());
    message.add("data", new JsonObject());
    return message;
  }
}
