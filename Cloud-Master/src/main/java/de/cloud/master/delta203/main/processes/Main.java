package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.main.Application;
import de.cloud.master.delta203.main.Cloud;
import de.cloud.master.delta203.main.commands.CreateGroup;
import de.cloud.master.delta203.main.commands.GroupInfo;

import java.util.ArrayList;
import java.util.List;

public class Main {

  private boolean shutdown;

  public Main() {
    shutdown = false;
  }

  public void run() {
    sendTypeHelp();
    while (!shutdown && Application.scanner.hasNextLine()) {
      String command = Application.scanner.nextLine();
      switch (command.split(" ")[0].toUpperCase()) {
        case "HELP":
          sendHelp();
          break;
        case "SHUTDOWN":
          shutdown = true;
          break;
        case "GROUPINFO":
          new GroupInfo(command).execute();
          break;
        case "CREATEGROUP":
          new CreateGroup().execute();
          break;
        default:
          sendTypeHelp();
          break;
      }
    }
    Application.scanner.close();
    Cloud.console.print("The cloud is now being shut down...");
  }

  private void sendTypeHelp() {
    Cloud.console.print("Type \"help\" to see all commands and information!");
  }

  private void sendHelp() {
    Cloud.console.print("Help and information:");
    Cloud.console.print("help » Shows help and information.");
    Cloud.console.print("shutdown » Shuts down the cloud.");
    Cloud.console.print("groupInfo <name> » Shows group information.");
    Cloud.console.print("createGroup » Create a proxy / server group.");
    Cloud.console.printRaw("");
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
  }
}
