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

package de.cloud.api.delta203.bungee.commands;

import de.cloud.api.delta203.bungee.CloudAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CloudCmdCloud extends Command {

  public CloudCmdCloud(String name) {
    super(name);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    TextComponent message =
        new TextComponent(
            ChatColor.BLUE
                + "This server is running CloudSystem version Cloud-Master:"
                + CloudAPI.plugin.getDescription().getVersion()
                + " by Delta203");
    sender.sendMessage(message);
  }
}
