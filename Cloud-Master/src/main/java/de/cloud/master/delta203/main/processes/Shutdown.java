package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.VServer;
import de.cloud.master.delta203.main.Application;
import de.cloud.master.delta203.main.Cloud;

import java.nio.file.Paths;

public class Shutdown {

  public Shutdown() {}

  public void run() {
    Application.scanner.close();
    for (VServer server : Cloud.services) {
      server.stopProcess();
    }
    Cloud.server.close();
    Cloud.pathManager.deleteDirectory(Paths.get(Cloud.pathManager.getPathServicesTemp()));
  }
}
