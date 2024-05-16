package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.main.Cloud;

public class ShowKey {

  public ShowKey() {}

  public void execute() {
    Cloud.console.print("Key: " + Cloud.key);
  }
}
