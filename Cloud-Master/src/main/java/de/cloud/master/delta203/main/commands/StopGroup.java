package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.main.Cloud;

public class StopGroup {

  private final String command;

  public StopGroup(String command) {
    this.command = command;
  }

  public void execute() {
    String[] args = command.split(" ");
    if (args.length != 2) {
      Cloud.console.print("Usage: stopGroup <group>");
      return;
    }
    String name = args[1];
    Group group = null;
    for (Group groups : Cloud.groups) {
      if (groups.getName().equals(name)) group = groups;
    }
    if (group == null) {
      Cloud.console.print("The name of group is invalid.");
      return;
    }
    for (Service service : Cloud.services.values()) {
      if (service.getServiceGroup() != group) continue;
      service.stopProcess();
    }
  }
}
