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

package de.cloud.api.delta203.core.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.cloud.api.delta203.proxy.CloudAPI;
import de.cloud.api.delta203.core.utils.CloudMessageType;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;

/** This class handles the server socket communication. */
public class CloudCommunication {

  public CloudCommunication() {}

  /**
   * This method checks if the message is empty.
   *
   * @param string the message
   * @return the message is empty
   */
  private boolean isEmpty(String string) {
    return (string == null || string.isEmpty());
  }

  /**
   * This method checks if the message is not in JSON format.
   *
   * @param string the message
   * @return the message is not in JSON format
   */
  private boolean notJson(String string) {
    try {
      JsonParser.parseString(string).getAsJsonObject();
      return false;
    } catch (Exception ignored) {
      return true;
    }
  }

  /**
   * This method checks if the message is a valid server, channel message.
   *
   * @param key the server socket key
   * @param string the message
   * @return the message is valid
   */
  public boolean isValid(String key, String string) {
    if (isEmpty(string)) return false;
    if (notJson(string)) return false;
    JsonObject message = JsonParser.parseString(string).getAsJsonObject();
    if (!message.has("key") || !message.has("type") || !message.has("data")) return false;
    return message.get("key").getAsString().equals(key);
  }

  /**
   * This method handles every valid message from the incoming channel.
   *
   * @param string the message
   */
  public void handle(String string) {
    JsonObject message = JsonParser.parseString(string).getAsJsonObject();
    switch (CloudMessageType.valueOf(message.get("type").getAsString())) {
      case ADDSERVER:
        {
          JsonObject data = message.get("data").getAsJsonObject();
          String name = data.get("name").getAsString();
          String ip = data.get("ip").getAsString();
          int port = data.get("port").getAsInt();
          CloudAPI.serverManager.addServer(name, Util.getAddr(ip + ":" + port));
          break;
        }
      case REMOVESERVER:
        {
          JsonObject data = message.get("data").getAsJsonObject();
          String name = data.get("name").getAsString();
          CloudAPI.serverManager.removeServer(name);
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
                    de.cloud.api.delta203.server.CloudAPI.plugin,
                    () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
          }
          break;
        }
    }
  }
}
