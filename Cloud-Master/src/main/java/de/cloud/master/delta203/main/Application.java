package de.cloud.master.delta203.main;

import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.main.processes.Main;
import de.cloud.master.delta203.main.processes.Setup;
import de.cloud.master.delta203.main.processes.Start;
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

  public void runMain() {
    Cloud.console.print("The cloud has started...");
    Cloud.console.print("Running current version: " + Constants.Locals.VERSION);
    runStart();
    new Main().run();
  }
}
