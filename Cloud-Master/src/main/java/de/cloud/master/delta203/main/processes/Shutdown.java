package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.main.Application;
import de.cloud.master.delta203.main.Cloud;

import java.nio.file.Paths;

public class Shutdown {

  public Shutdown() {}

  public void run() {
    Cloud.console.print("The cloud is stopping...");
    Application.scanner.close();
    for (Service service : Cloud.services.values()) {
      service.stopProcess();
    }
    while (aServiceIsActive()) {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    Cloud.server.close();
    Cloud.pathManager.deleteDirectory(Paths.get(Cloud.pathManager.getPathServicesTemp()));
  }

  private boolean aServiceIsActive() {
    boolean active = false;
    for (Service service : Cloud.services.values()) {
      if (service.isProcessAlive()) {
        active = true;
        break;
      }
    }
    return active;
  }
}
