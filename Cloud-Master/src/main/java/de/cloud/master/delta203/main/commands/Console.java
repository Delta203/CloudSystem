package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.main.Cloud;

public class Console {

  private final String command;

  public Console(String command) {
    this.command = command;
  }

  public void execute() {
    String[] args = command.split(" ");
    if (args.length != 2) {
      Cloud.console.print("Usage: console <service>");
      return;
    }
    String name = args[1];
    if (!Cloud.services.containsKey(name)) {
      Cloud.console.print("The name of service is invalid.");
      return;
    }
    Service service = Cloud.services.get(name);
    Cloud.console.print("Toggled console log of service " + name + ".");
    service.debug = !service.debug;
  }
}
