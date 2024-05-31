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

package de.cloud.api.delta203.bungee.utils;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;

/** This class manages the incoming servers and registers them as fallbacks if necessary. */
public class ServerManager {

  private final List<ServerInfo> fallbacks;
  private final String fallbackPrefix = "lobby";

  /** Create a server manager to add and remove servers from proxy. */
  public ServerManager() {
    fallbacks = new ArrayList<>();
  }

  /**
   * This method checks if server is a fallback server.
   *
   * @param serverInfo the server to be checked
   * @return server is a fallback
   */
  public boolean isFallback(ServerInfo serverInfo) {
    return fallbacks.contains(serverInfo);
  }

  /**
   * This method gets a fallback server randomly.
   *
   * @return a random fallback server
   */
  public ServerInfo getRandomFallback() {
    return fallbacks.get(new Random().nextInt(fallbacks.size()));
  }

  /** This method updates the proxy fallback config. */
  private void updateFallbacks() {
    for (ListenerInfo info : ProxyServer.getInstance().getConfig().getListeners()) {
      info.getServerPriority().clear();
      for (ServerInfo serverInfo : fallbacks) {
        info.getServerPriority().add(serverInfo.getName());
      }
    }
  }

  /**
   * This method adds a new server to proxy main config.
   *
   * @param name the server name
   * @param address the server address
   */
  public void addServer(String name, SocketAddress address) {
    ServerInfo serverInfo =
        ProxyServer.getInstance().constructServerInfo(name, address, "Cloud Server", false);
    ProxyServer.getInstance().getServers().put(name, serverInfo);
    if (name.toLowerCase().startsWith(fallbackPrefix)) {
      // server is a fallback
      fallbacks.add(serverInfo);
      updateFallbacks();
    }
    String suffix = fallbacks.contains(serverInfo) ? "*" : "";
    System.out.println("+ " + name + suffix + " (" + address.toString() + ")");
  }

  /**
   * This method removes a server from the proxy main config.
   *
   * @param name the server name
   */
  public void removeServer(String name) {
    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(name);
    ProxyServer.getInstance().getServers().remove(name);
    if (name.toLowerCase().startsWith(fallbackPrefix)) {
      // server is a fallback
      fallbacks.remove(serverInfo);
      updateFallbacks();
    }
    System.out.println("- " + name);
  }
}
