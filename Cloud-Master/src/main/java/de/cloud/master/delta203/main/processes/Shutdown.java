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

package de.cloud.master.delta203.main.processes;

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.main.Application;
import de.cloud.master.delta203.main.Cloud;

import java.nio.file.Paths;

public class Shutdown {

  public Shutdown() {}

  public void run() {
    Cloud.console.print("The cloud is stopping...");
    Application.scanner.close();
    for (Service service : Cloud.services.values()) {
      service.stopProcess();
    }
    while (aServiceIsActive()) {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    Cloud.server.close();
    Cloud.pathManager.deleteDirectory(Paths.get(Cloud.pathManager.getPathServicesTemp()));
  }

  private boolean aServiceIsActive() {
    boolean active = false;
    for (Service service : Cloud.services.values()) {
      if (service.isProcessAlive()) {
        active = true;
        break;
      }
    }
    return active;
  }
}
