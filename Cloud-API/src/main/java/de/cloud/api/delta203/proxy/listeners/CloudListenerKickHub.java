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

package de.cloud.api.delta203.proxy.listeners;

import de.cloud.api.delta203.proxy.CloudAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * This class ensures that a player is not kicked directly from the network if a server stops. A
 * matching fallback server is searched and the player is directed to it when one is found.
 */
public class CloudListenerKickHub implements Listener {

  @EventHandler
  public void onKick(ServerKickEvent e) {
    ServerInfo server = e.getKickedFrom();
    ServerInfo fallback = CloudAPI.serverManager.getRandomFallback(server);
    if (fallback != null) {
      e.setCancelServer(fallback);
      e.setCancelled(true);
      return;
    }
    // kick player from network
    e.setKickReasonComponent(
        new BaseComponent[] {
          new TextComponent(ChatColor.RED + "Could not connect to a default or fallback server!")
        });
  }
}
