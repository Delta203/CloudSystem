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

package de.cloud.api.delta203.server;

import de.cloud.api.delta203.core.CloudChannel;
import de.cloud.api.delta203.core.CloudInstance;
import de.cloud.api.delta203.core.packets.CloudPacketConnect;
import de.cloud.api.delta203.core.packets.CloudPacketInGame;
import de.cloud.api.delta203.core.utils.CloudServiceState;
import de.cloud.api.delta203.server.commands.CloudCmdServiceInfo;
import de.cloud.api.delta203.server.commands.CloudCmdUpdateState;
import de.cloud.api.delta203.server.listeners.CloudListenerOnlyProxy;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class CloudAPI extends JavaPlugin {

  /** Get the Cloud-API plugin instance. */
  public static CloudAPI plugin;

  /** Get the Cloud-API file configuration. */
  public static Configuration config;

  private static CloudServiceState state;

  @Override
  public void onEnable() {
    plugin = this;
    loadConfig();
    state = CloudServiceState.LOBBY;
    CloudInstance.services = new HashMap<>();
    CloudInstance.services.put(CloudServiceState.PROXY, new ArrayList<>());
    CloudInstance.services.put(CloudServiceState.LOBBY, new ArrayList<>());
    CloudInstance.services.put(CloudServiceState.INGAME, new ArrayList<>());

    CloudInstance.name = config.getString("name");
    CloudInstance.ip = config.getString("server.ip");
    CloudInstance.port = config.getInt("server.port");
    CloudInstance.key = config.getString("server.key");

    connect();

    getCommand("serviceInfo").setExecutor(new CloudCmdServiceInfo());
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
    CloudInstance.channel =
        new CloudChannel(
            CloudInstance.name, CloudInstance.ip, CloudInstance.port, CloudInstance.key);
    if (!CloudInstance.channel.connect()) return;
    // send connection packet
    CloudPacketConnect packetConnect = new CloudPacketConnect();
    packetConnect.k(CloudInstance.key);
    packetConnect.n(CloudInstance.name);
    CloudInstance.channel.sendMessage(packetConnect.message());
    // start main thread
    CloudInstance.channel.start();
  }

  /**
   * This method gets the Cloud-Service state.
   *
   * @return the service state
   */
  public static CloudServiceState getServiceState() {
    return state;
  }

  /** This method sets the {@link CloudServiceState} to INGAME. */
  public static void updateServiceState() {
    state = CloudServiceState.INGAME;
    CloudPacketInGame packetInGame = new CloudPacketInGame();
    packetInGame.k(CloudInstance.key);
    CloudInstance.channel.sendMessage(packetInGame.message());
  }
}
