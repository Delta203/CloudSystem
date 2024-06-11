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

import de.cloud.api.delta203.core.CloudChannel;
import de.cloud.api.delta203.core.packets.CloudPacketConnect;
import de.cloud.api.delta203.core.packets.CloudPacketInGame;
import de.cloud.api.delta203.core.utils.CloudServerState;
import de.cloud.api.delta203.spigot.commands.CloudCmdUpdateState;
import de.cloud.api.delta203.spigot.listeners.CloudListenerOnlyProxy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class CloudAPI extends JavaPlugin {

  /** Get the Cloud-API plugin instance. */
  public static CloudAPI plugin;

  /** Get the Cloud-API file configuration. */
  public static Configuration config;

  /** Get the Cloud-Service name. */
  public static String name;

  /** Get the Cloud-Service channel. */
  public static CloudChannel channel;

  private static CloudServerState state;

  /** Get the Cloud-Service ip address. */
  public static String serverIp;

  private int serverPort;
  private String serverKey;

  @Override
  public void onEnable() {
    plugin = this;
    state = CloudServerState.LOBBY;
    loadConfig();

    name = config.getString("name");
    serverIp = config.getString("server.ip");
    serverPort = config.getInt("server.port");
    serverKey = config.getString("server.key");

    connect();

    getCommand("updateState").setExecutor(new CloudCmdUpdateState());
    Bukkit.getPluginManager().registerEvents(new CloudListenerOnlyProxy(), plugin);
  }

  private void loadConfig() {
    CloudFileManager configYml = new CloudFileManager("config.yml");
    configYml.create();
    configYml.load();
    config = configYml.get();
  }

  private void connect() {
    channel = new CloudChannel(name, serverIp, serverPort, serverKey);
    if (!channel.connect()) return;
    // send connection packet
    CloudPacketConnect packetConnect = new CloudPacketConnect();
    packetConnect.k(serverKey);
    packetConnect.s(name);
    channel.sendMessage(packetConnect.message());
    // start main thread
    channel.start();
  }

  /**
   * This method gets the cloud service state.
   *
   * @return the service state
   */
  public static CloudServerState getServiceState() {
    return state;
  }

  /** This method sets the {@link CloudServerState} to INGAME. */
  public static void updateServiceState() {
    state = CloudServerState.INGAME;
    CloudPacketInGame packetInGame = new CloudPacketInGame();
    packetInGame.k(plugin.serverKey);
    channel.sendMessage(packetInGame.message());
  }
}
