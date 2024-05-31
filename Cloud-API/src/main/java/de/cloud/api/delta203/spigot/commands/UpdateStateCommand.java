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

package de.cloud.api.delta203.spigot.commands;

import de.cloud.api.delta203.core.utils.ServerState;
import de.cloud.api.delta203.spigot.CloudAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * This class is a command executor and sets the current server state to INGAME. It will be used
 * mostly for test purposes.
 */
public class UpdateStateCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!sender.isOp()) {
      sender.sendMessage(ChatColor.RED + "You must be op to execute the command!");
      return false;
    }
    if (CloudAPI.getServiceState() == ServerState.INGAME) return false;
    CloudAPI.updateServiceState();
    sender.sendMessage(ChatColor.GREEN + "Successfully set the server state to INGAME.");
    return false;
  }
}
