package de.cloud.master.delta203.core.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.cloud.master.delta203.core.utils.MessageType;
import de.cloud.master.delta203.main.sockets.Channel;

public class Messages {

  private final Channel channel;

  public Messages(Channel channel) {
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
        channel.initialise(
            message.get("data").getAsJsonObject().get("name").getAsString(),
            message.get("data").getAsJsonObject().get("port").getAsInt());
        break;
      case INGAME:
        System.out.println(MessageType.INGAME);
        break;
    }
  }
}
