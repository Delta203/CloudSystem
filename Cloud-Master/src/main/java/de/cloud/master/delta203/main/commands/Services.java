package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.main.Cloud;

public class Services {

  public Services() {}

  public void execute() {
    Cloud.console.print("The list of active services:");
    if (Cloud.services.isEmpty()) {
      Cloud.console.print("There is currently no active service.");
    }
    for (Service service : Cloud.services.values()) {
      boolean connected = service.getServiceChannel() != null;
      Cloud.console.print(
          "- "
              + service.getServiceName()
              + ":"
              + service.getServicePort()
              + " ["
              + (connected ? "§aCONNECTED§r" : "WAITING")
              + "]");
    }
  }
}
