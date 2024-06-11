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

package de.cloud.api.delta203.bungee;

import de.cloud.api.delta203.bungee.commands.CloudCmdCloud;
import de.cloud.api.delta203.bungee.commands.CloudCmdLobby;
import de.cloud.api.delta203.bungee.utils.CloudServerManager;
import de.cloud.api.delta203.core.CloudChannel;
import de.cloud.api.delta203.core.packets.CloudPacketConnect;
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

  /** Get the Cloud-Service name. */
  public static String name;

  /** Get the Cloud-Service channel. */
  public static CloudChannel channel;

  private String serverIp;
  private int serverPort;
  private String serverKey;

  @Override
  public void onEnable() {
    plugin = this;
    loadConfig();
    serverManager = new CloudServerManager();

    name = config.getString("name");
    serverIp = config.getString("server.ip");
    serverPort = config.getInt("server.port");
    serverKey = config.getString("server.key");

    connect();

    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(plugin, new CloudCmdCloud("cloud"));
    ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new CloudCmdLobby("l"));
    ProxyServer.getInstance()
        .getPluginManager()
        .registerCommand(plugin, new CloudCmdLobby("lobby"));
    ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new CloudCmdLobby("hub"));
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
    System.out.println(packetConnect.message());
    channel.sendMessage(packetConnect.message());
    // start main thread
    channel.start();
  }
}
