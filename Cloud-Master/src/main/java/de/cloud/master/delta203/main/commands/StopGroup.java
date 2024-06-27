/*
 * Copyright 2024 Cloud System by Delta203
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.main.Cloud;

public class StopGroup {

  private final String command;

  public StopGroup(String command) {
    this.command = command;
  }

  public void execute() {
    String[] args = command.split(" ");
    if (args.length != 2) {
      Cloud.console.print("Usage: stopGroup <group>");
      return;
    }
    String name = args[1];
    Group group = null;
    for (Group groups : Cloud.groups) {
      if (groups.getName().equals(name)) group = groups;
    }
    if (group == null) {
      Cloud.console.print("The name of group is invalid.");
      return;
    }
    for (Service service : Cloud.services.values()) {
      if (service.getServiceGroup() != group) continue;
      service.stopProcess();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
