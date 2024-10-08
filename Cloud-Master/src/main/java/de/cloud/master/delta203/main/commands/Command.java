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

package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.core.packets.PacketCommand;
import de.cloud.master.delta203.main.Cloud;

public class Command {

  private final String command;

  public Command(String command) {
    this.command = command;
  }

  public void execute() {
    String[] args = command.split(" ");
    if (args.length <= 2) {
      Cloud.console.print("Usage: command <service> <args>");
      return;
    }
    String name = args[1];
    if (!Cloud.services.containsKey(name)) {
      Cloud.console.print("The name of service is invalid.");
      return;
    }
    String cmd = command.replace(args[0] + " " + args[1] + " ", "");
    Service service = Cloud.services.get(name);
    if (service.getServiceChannel() == null) {
      Cloud.console.print("The service channel is not connected.");
      return;
    }
    PacketCommand packetCommand = new PacketCommand();
    packetCommand.c(cmd);
    service.getServiceChannel().sendMessage(packetCommand.message());
    Cloud.console.print("The command was successfully sent!");
  }
}
