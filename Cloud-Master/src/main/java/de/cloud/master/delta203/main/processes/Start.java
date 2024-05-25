package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.core.files.FileManager;
import de.cloud.master.delta203.core.security.KeyGenerator;
import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.main.Cloud;
import de.cloud.master.delta203.main.sockets.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class Start {

  public Start() {
    Cloud.groups = new ArrayList<>();
  }

  private void config() {
    Cloud.config.load();
  }

  private void groups() {
    File[] templates = new File(Cloud.pathManager.getPathDataGroup()).listFiles();
    if (templates == null) return;
    for (File file : templates) {
      String name = file.getName();
      FileManager fileManager = new FileManager(Cloud.pathManager.getPathDataGroup(), name);
      Group group = new Group(fileManager);
      group.load();
      Cloud.groups.add(group);
    }
  }

  private void key() {
    KeyGenerator generator = new KeyGenerator();
    generator.generate();
    Cloud.key = generator.getKey();
  }

  private void api() {
    if (new File(Cloud.pathManager.getPathAssetsAPI()).exists()) {
      File[] api =
          Objects.requireNonNull(new File(Cloud.pathManager.getPathAssetsAPI()).listFiles());
      if (api.length == 0) {
        Cloud.console.print("§cAdd a Cloud-API into: " + Cloud.pathManager.getPathAssetsAPI());
        System.exit(0);
      } else {
        Constants.Locals.API = api[0].getName();
        Cloud.console.print("Running current api version: " + Constants.Locals.API);
      }
    } else {
      Cloud.console.print("§cAdd a Cloud-API into: " + Cloud.pathManager.getPathAssetsAPI());
      System.exit(0);
    }
  }

  private void server() {
    try {
      Cloud.server = new Server();
      Cloud.server.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void service() {
    Cloud.pathManager.deleteDirectory(Paths.get(Cloud.pathManager.getPathServicesTemp()));
    Cloud.services = new ArrayList<>();
    for (Group group : Cloud.groups) {
      for (int i = 0; i < group.getMinAmount(); i++) {
        Service service = new Service(group);
        if (service.register()) service.start();
      }
    }
  }

  public void run() {
    config();
    groups();
    key();
    api();
    Cloud.console.print("All data has been loaded successfully.");
    server();
    service();
  }
}
