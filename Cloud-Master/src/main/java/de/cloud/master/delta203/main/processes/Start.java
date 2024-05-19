package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.core.files.FileManager;
import de.cloud.master.delta203.core.security.KeyGenerator;
import de.cloud.master.delta203.main.Cloud;
import de.cloud.master.delta203.main.sockets.Server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    // TODO: Remove debug key!!!
    Cloud.key = "key";
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
    new Service().register();
  }

  public void run() {
    config();
    groups();
    key();
    Cloud.console.print("All data has been loaded successfully.");
    server();
    service();
  }
}
