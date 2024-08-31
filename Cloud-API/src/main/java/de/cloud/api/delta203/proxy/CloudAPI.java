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

package de.cloud.api.delta203.proxy;

import de.cloud.api.delta203.core.CloudChannel;
import de.cloud.api.delta203.core.CloudInstance;
import de.cloud.api.delta203.core.packets.CloudPacketConnect;
import de.cloud.api.delta203.core.utils.CloudServiceState;
import de.cloud.api.delta203.proxy.commands.CloudCmdCloud;
import de.cloud.api.delta203.proxy.commands.CloudCmdLobby;
import de.cloud.api.delta203.proxy.listeners.CloudListenerKickHub;
import de.cloud.api.delta203.proxy.utils.CloudServerManager;
import java.util.ArrayList;
import java.util.HashMap;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class CloudAPI extends Plugin {

  /** Get the Cloud-API plugin instance. */
  public static CloudAPI plugin;

  /** Get the Cloud-API file configuration. */
  public static Configuration config;

  /** Get the Cloud-API server manager. */
  public static CloudServerManager serverManager;

  @Override
  public void onEnable() {
    plugin = this;
    loadConfig();
    serverManager = new CloudServerManager();

    CloudInstance.name = config.getString("name");
    CloudInstance.ip = config.getString("server.ip");
    CloudInstance.port = config.getInt("server.port");
    CloudInstance.key = config.getString("server.key");
    CloudInstance.state = CloudServiceState.PROXY;

    CloudInstance.services = new HashMap<>();
    CloudInstance.services.put(CloudServiceState.PROXY, new ArrayList<>());
    CloudInstance.services.put(CloudServiceState.LOBBY, new ArrayList<>());
    CloudInstance.services.put(CloudServiceState.INGAME, new ArrayList<>());

    connect();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(plugin, new CloudCmdCloud("cloud"));
    ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new CloudCmdLobby("l"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(plugin, new CloudCmdLobby("lobby"));
    ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new CloudCmdLobby("hub"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(plugin, new CloudListenerKickHub());
  }

  @Override
  public void onDisable() {
    // Essential manuel socket disconnect
    CloudInstance.channel.disconnect();
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
}
