package de.cloud.master.delta203.main;

import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.main.processes.Main;
import de.cloud.master.delta203.main.processes.Setup;
import de.cloud.master.delta203.main.processes.Start;
import de.cloud.master.delta203.main.server.Server;

import java.io.IOException;
import java.util.Scanner;

public class Application {

  public static Scanner scanner;

  public Application() {
    scanner = new Scanner(System.in);
    Cloud.console.clear();
  }

  public void runSetup() {
    new Setup().run();
  }

  private void runStart() {
    new Start().run();
  }

  private void runServer() {
    try {
      Cloud.server = new Server();
      Cloud.server.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void runMain() {
    Cloud.console.print("The cloud has started...");
    Cloud.console.print("Running current version: " + Constants.Locals.VERSION);
    runStart();
    runServer();
    new Main().run();
  }
}
