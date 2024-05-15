package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.core.files.FileManager;
import de.cloud.master.delta203.main.Cloud;

import java.io.File;

public class Start {

  public Start() {}

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

  public void run() {
    config();
    groups();
    Cloud.console.print("All data has been loaded successfully.");
  }
}
