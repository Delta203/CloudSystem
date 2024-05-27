package de.cloud.api.delta203.core.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.cloud.api.delta203.bungee.CloudAPI;
import de.cloud.api.delta203.core.utils.MessageType;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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
    JsonObject message = JsonParser.parseString(string).getAsJsonObject();
    switch (MessageType.valueOf(message.get("type").getAsString())) {
      case ADDSERVER:
        {
          JsonObject data = message.get("data").getAsJsonObject();
          String name = data.get("name").getAsString();
          String ip = data.get("ip").getAsString();
          int port = data.get("port").getAsInt();
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
      case COMMAND:
        {
          JsonObject data = message.get("data").getAsJsonObject();
          String command = data.get("command").getAsString();
          if (CloudAPI.plugin != null) {
            // proxy server
            ProxyServer.getInstance()
                .getPluginManager()
                .dispatchCommand(ProxyServer.getInstance().getConsole(), command);
          } else {
            // server server
            Bukkit.getScheduler()
                .runTask(
                    de.cloud.api.delta203.spigot.CloudAPI.plugin,
                    () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
          }
          break;
        }
    }
  }

  /*
   * Message builders
   */

  public JsonObject connectMessage(String name) {
    JsonObject message = new JsonObject();
    message.addProperty("key", key);
    message.addProperty("type", MessageType.CONNECT.name());
    JsonObject data = new JsonObject();
    data.addProperty("name", name);
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
