package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.core.Service;
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
    service
        .getServiceChannel()
        .sendMessage(
            service.getServiceChannel().getCommunication().dispatchCommandMessage(cmd).toString());
    Cloud.console.print("The command was successfully sent!");
  }
}
