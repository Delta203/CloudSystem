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

package de.cloud.api.delta203.server.commands;

import de.cloud.api.delta203.core.CloudInstance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * This class is a command executor and shows the current services info and every info about
 * registered services. It will be used mostly for test purposes.
 */
public class CloudCmdServiceInfo implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    /* Crash server for test only!!!
    if (sender instanceof Player p) {
      for (int i = 0; i < 100000; i++) {
        p.getWorld().spawnEntity(p.getLocation(), EntityType.PIG);
      }
    } */
    if (!sender.isOp()) {
      sender.sendMessage(ChatColor.RED + "You must be op to execute the command!");
      return false;
    }
    sender.sendMessage(
        CloudInstance.name
            + ":"
            + Bukkit.getServer().getPort()
            + " ["
            + CloudInstance.state.name()
            + "]");
    sender.sendMessage(CloudInstance.services.toString());
    return false;
  }
}
