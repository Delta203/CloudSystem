package de.cloud.master.delta203.main;

import de.cloud.master.delta203.core.Console;
import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.core.files.FileManager;
import de.cloud.master.delta203.core.files.PathManager;

import java.util.ArrayList;
import java.util.List;

public class Cloud {

  public static PathManager pathManager;
  public static FileManager config;

  public static Console console;

  public static int memory = 0;
  public static List<Group> groups = new ArrayList<>();

  public static void main(String[] args) {
    pathManager = new PathManager();
    boolean doSetup = pathManager.mkdir();
    config = new FileManager(pathManager.getPathData(), "config.json");
    console = new Console();

    Application application = new Application();
    if (doSetup) application.runSetup();
    application.runMain();
  }
}
