package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.main.Application;
import de.cloud.master.delta203.main.Cloud;

public class CreateGroup {

  public CreateGroup() {}

  private String name() {
    Cloud.console.print("What should the name of the group be?");
    String name = Application.scanner.nextLine();
    while (name.equals("default")) {
      Cloud.console.print("The name of group can not be \"default\".");
      name = Application.scanner.nextLine();
    }
    return name;
  }

  private GroupType type() {
    Cloud.console.print(
        "What type of group is this? ["
            + GroupType.PROXY.name()
            + ", "
            + GroupType.SERVER.name()
            + "]");
    String type = Application.scanner.nextLine();
    while (!type.equals(GroupType.PROXY.name()) && !type.equals(GroupType.SERVER.name())) {
      Cloud.console.print("The specified type of group is invalid.");
      type = Application.scanner.nextLine();
    }
    return GroupType.valueOf(type);
  }

  private int memory() {
    Cloud.console.print("How much memory should the group have? (MB)");
    int memory = 0;
    while (memory == 0) {
      try {
        memory = Integer.decode(Application.scanner.nextLine());
      } catch (NumberFormatException e) {
        Cloud.console.print("You must enter a number!");
      }
    }
    return memory;
  }

  private boolean statisch() {
    Cloud.console.print("Should the group be static? [true, false]");
    String statisch = Application.scanner.nextLine();
    while (!statisch.equals("true") && !statisch.equals("false")) {
      Cloud.console.print("You must enter a boolean!");
      statisch = Application.scanner.nextLine();
    }
    return Boolean.parseBoolean(statisch);
  }

  private int minAmount() {
    Cloud.console.print("How many servers should run permanently?");
    int amount = 0;
    while (amount == 0) {
      try {
        amount = Integer.decode(Application.scanner.nextLine());
      } catch (NumberFormatException e) {
        Cloud.console.print("You must enter a number!");
      }
    }
    return amount;
  }

  private int maxAmount() {
    Cloud.console.print("What is the maximum amount of servers you want to run?");
    int amount = 0;
    while (amount == 0) {
      try {
        amount = Integer.decode(Application.scanner.nextLine());
      } catch (NumberFormatException e) {
        Cloud.console.print("You must enter a number!");
      }
    }
    return amount;
  }

  private boolean confirmed(
      String name, GroupType type, int memory, int minAmount, int maxAmount, boolean statisch) {
    Cloud.console.print(
        "Press enter to create the group or write \"cancel\". ("
            + name
            + ", "
            + type.name()
            + ", "
            + memory
            + ", "
            + minAmount
            + ", "
            + maxAmount
            + ", "
            + statisch
            + ")");
    if (Application.scanner.nextLine().equals("cancel")) {
      Cloud.console.print("§cThe group has not been created!");
      return false;
    }
    return true;
  }

  public void execute() {
    Cloud.console.print("Create a group:");
    String name = name();
    GroupType type = type();
    int memory = memory();
    int minAmount = 1;
    int maxAmount = 1;
    boolean statisch = false;
    if (type == GroupType.SERVER) {
      statisch = statisch();
      if (!statisch) {
        minAmount = minAmount();
        maxAmount = maxAmount();
      }
    }
    if (!confirmed(name, type, memory, minAmount, maxAmount, statisch)) return;
    Group group = new Group(name, type, memory, minAmount, maxAmount, statisch);
    group.create();
    Cloud.groups.add(group);
    Cloud.console.print("Configurations are saved...");
    Cloud.console.print("§aThe group has been created and registered!");
    // start services
    group.runServices();
  }
}
