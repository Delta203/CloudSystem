package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.main.Cloud;

public class GroupInfo {

  private final String command;

  public GroupInfo(String command) {
    this.command = command;
  }

  public void execute() {
    String[] args = command.split(" ");
    if (args.length != 2) {
      Cloud.console.print("Usage: groupInfo <name>");
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
    Cloud.console.print("Group information: " + group.getName());
    Cloud.console.print("Type: " + group.getType().name());
    Cloud.console.print("Memory: " + group.getMemory() + " MB");
    Cloud.console.print("Min amount: " + group.getMinAmount());
    Cloud.console.print("Max amount: " + group.getMaxAmount());
    Cloud.console.print("Static: " + group.isStatic());
    Cloud.console.print("Maintenance: " + group.isMaintenance());
  }
}
