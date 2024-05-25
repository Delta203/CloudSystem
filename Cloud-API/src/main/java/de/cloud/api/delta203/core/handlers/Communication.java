package de.cloud.api.delta203.core.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.cloud.api.delta203.bungee.CloudAPI;
import de.cloud.api.delta203.core.utils.MessageType;
import net.md_5.bungee.Util;

public class Communication {

  private final String key;

  public Communication(String key) {
    this.key = key;
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
    if (CloudAPI.plugin == null) return;
    JsonObject message = JsonParser.parseString(string).getAsJsonObject();
    switch (MessageType.valueOf(message.get("type").getAsString())) {
      case ADDSERVER:
        {
          JsonObject data = message.get("data").getAsJsonObject();
          String name = data.get("name").getAsString();
          String ip = data.get("ip").getAsString();
          int port = data.get("port").getAsInt();
          if (port == 25565) return;
          CloudAPI.addServer(name, Util.getAddr(ip + ":" + port));
          break;
        }
      case REMOVESERVER:
        {
          JsonObject data = message.get("data").getAsJsonObject();
          String name = data.get("name").getAsString();
          CloudAPI.removeServer(name);
          break;
        }
    }
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
