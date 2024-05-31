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

import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.main.Cloud;

public class Services {

  public Services() {}

  public void execute() {
    Cloud.console.print("The list of active services:");
    if (Cloud.services.isEmpty()) {
      Cloud.console.print("There is currently no active service.");
    }
    for (Service service : Cloud.services.values()) {
      boolean connected = service.getServiceChannel() != null;
      Cloud.console.print(
          "- "
              + service.getServiceName()
              + ":"
              + service.getServicePort()
              + " ["
              + (connected ? "§aCONNECTED§r" : "WAITING")
              + "] ["
              + service.getServiceState().name()
              + "]");
    }
  }
}
