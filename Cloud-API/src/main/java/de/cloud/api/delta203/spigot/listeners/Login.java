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

package de.cloud.api.delta203.spigot.listeners;

import de.cloud.api.delta203.spigot.CloudAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * This class checks if the player joins with the valid proxy server. Nevertheless, it is
 * recommended to install an additional firewall.
 */
public class Login implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onLogin(PlayerLoginEvent e) {
    System.out.println(CloudAPI.serverIp);
    System.out.println(e.getAddress().getHostAddress());
    if (e.getAddress().getHostAddress().equals(CloudAPI.serverIp)
        || e.getAddress().getHostAddress().equals("localhost")
        || e.getAddress().getHostAddress().equals("127.0.0.1")) return;
    e.disallow(
        PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You must join through the proxy.");
  }
}
