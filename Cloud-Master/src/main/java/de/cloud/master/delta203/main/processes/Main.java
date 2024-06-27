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

package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.main.Application;
import de.cloud.master.delta203.main.Cloud;
import de.cloud.master.delta203.main.commands.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public Main() {}

  public void run() {
    sendTypeHelp();
    while (!Cloud.shutdown && Application.scanner.hasNextLine()) {
      String command = Application.scanner.nextLine();
      switch (command.split(" ")[0].toUpperCase()) {
        case "HELP":
          sendHelp();
          break;
        case "SHUTDOWN":
          Cloud.shutdown = true;
          break;
        case "CREATEGROUP":
          new CreateGroup().execute();
          break;
        case "GROUPINFO":
          new GroupInfo(command).execute();
          break;
        case "CONSOLE":
          new Console(command).execute();
          break;
        case "COMMAND":
          new Command(command).execute();
          break;
        case "STOP":
          new Stop(command).execute();
          break;
        case "STOPGROUP":
          new StopGroup(command).execute();
          break;
        case "SERVICES":
          new Services().execute();
          break;
        case "OS":
          new OperatingSystem().execute();
          break;
        case "SHOWKEY":
          new ShowKey().execute();
          break;
        case "RELOAD":
          new Reload().execute();
          break;
        default:
          sendTypeHelp();
          break;
      }
    }
    new Shutdown().run();
    Cloud.console.print("Good bye!");
  }

  private void sendTypeHelp() {
    Cloud.console.print("Type \"help\" to see all commands and information!");
  }

  private void sendHelp() {
    Cloud.console.print("§fshutdown§r - Shuts down the cloud.");
    Cloud.console.print("§fcreateGroup§r - Create a proxy / server group.");
    Cloud.console.print("§fgroupInfo <group>§r - Shows group information.");
    Cloud.console.print("§fconsole <service>§r - Shows the server console of service.");
    Cloud.console.print("§fcommand <service> <args>§r - Dispatch a command on a service.");
    Cloud.console.print("§fstop <service>§r - Stop a specific service.");
    Cloud.console.print("§fstopGroup <group>§r - Stop a server group.");
    Cloud.console.print("§fservices§r - Shows the list of active services.");
    Cloud.console.print("§fos§r - Shows the operating system data.");
    Cloud.console.print("§fshowKey§r - Shows the communication key.");
    Cloud.console.print("§freload§r - Reload cloud configuration data.");
    Cloud.console.printRaw("");
    Cloud.console.print("Host: " + Cloud.server.getIp() + ":" + Cloud.server.getPort());
    Cloud.console.print(
        "Memory: "
            + Cloud.memory
            + " / "
            + Cloud.config.getData().get("maxMemory").getAsInt()
            + " MB");
    List<String> groupNames = new ArrayList<>();
    for (Group group : Cloud.groups) {
      groupNames.add(group.getName());
    }
    Cloud.console.print("Groups: " + groupNames);
    Cloud.console.print("Services: " + Cloud.services.size());
  }
}
