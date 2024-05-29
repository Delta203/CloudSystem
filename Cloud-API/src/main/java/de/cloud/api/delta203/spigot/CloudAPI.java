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

package de.cloud.api.delta203.spigot;

import de.cloud.api.delta203.core.Channel;
import de.cloud.api.delta203.core.utils.ServerState;
import de.cloud.api.delta203.spigot.listeners.Login;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.Socket;

public class CloudAPI extends JavaPlugin {

  /** Get the Cloud-API plugin instance. */
  public static CloudAPI plugin;

  /** Get the Cloud-API file configuration. */
  public static Configuration config;

  private static String name;
  private static ServerState state;

  public static String serverIp;
  private int serverPort;
  private String serverKey;
  private Channel channel;

  @Override
  public void onEnable() {
    plugin = this;
    state = ServerState.LOBBY;
    loadConfig();

    name = config.getString("name");
    serverIp = config.getString("server.ip");
    serverPort = config.getInt("server.port");
    serverKey = config.getString("server.key");

    connect();

    Bukkit.getPluginManager().registerEvents(new Login(), plugin);
  }

  @Override
  public void onDisable() {
    Socket socket = channel.getSocket();
    if (socket == null || !socket.isConnected()) return;
    try {
      socket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void loadConfig() {
    FileManager configYml = new FileManager("config.yml");
    configYml.create();
    configYml.load();
    config = configYml.get();
  }

  private void connect() {
    channel = new Channel(serverIp, serverPort, serverKey);
    channel.connect(name);
  }

  /**
   * This method gets the service name.
   *
   * @return the name of service
   */
  public static String getServiceName() {
    return name;
  }

  /**
   * This method gets the service server state.
   *
   * @return the server state
   */
  public static ServerState getServiceState() {
    return state;
  }

  /** This method sets the {@link ServerState} to INGAME. */
  public static void updateServiceState() {
    state = ServerState.INGAME;
  }
}
