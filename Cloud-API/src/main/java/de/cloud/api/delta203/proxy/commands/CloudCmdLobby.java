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

package de.cloud.api.delta203.proxy.commands;

import de.cloud.api.delta203.proxy.CloudAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CloudCmdLobby extends Command {

  public CloudCmdLobby(String name) {
    super(name);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (sender instanceof ProxiedPlayer p) {
      if (CloudAPI.serverManager.isFallback(p.getServer().getInfo())) {
        // player is already on a lobby server
        p.sendMessage(new TextComponent(ChatColor.RED + "You are already on a lobby server!"));
        return;
      }
      p.connect(CloudAPI.serverManager.getRandomFallback());
    }
  }
}
