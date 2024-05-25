package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.main.Application;
import de.cloud.master.delta203.main.Cloud;

import java.nio.file.Paths;

public class Shutdown {

  public Shutdown() {}

  public void run() {
    Application.scanner.close();
    for (Service service : Cloud.services) {
      service.stopProcess();
    }
    Cloud.console.print("The cloud stops in 5 seconds...");
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    Cloud.server.close();
    Cloud.pathManager.deleteDirectory(Paths.get(Cloud.pathManager.getPathServicesTemp()));
  }
}
