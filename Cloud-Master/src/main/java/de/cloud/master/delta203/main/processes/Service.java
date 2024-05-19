package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.core.VServer;
import de.cloud.master.delta203.main.Cloud;
import java.util.ArrayList;

public class Service {

  public Service() {
    Cloud.services = new ArrayList<>();
  }

  public void register() {
    for (Group group : Cloud.groups) {
      for (int i = 0; i < group.getMinAmount(); i++) {
        VServer server = new VServer(group);
        server.register();
        server.copyFiles();
        server.addFiles();
        server.runProcess();
      }
    }
  }
}
