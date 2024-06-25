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

package de.cloud.master.delta203.core.packets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.core.utils.MessageType;
import de.cloud.master.delta203.main.Cloud;
import java.util.List;

/** This class is a cloud packet. It is sent to the proxy socket to add a server. */
public class PacketServiceInfo {

  public static final MessageType type = MessageType.SERVICEINFO;

  private List<Service> services;

  public PacketServiceInfo() {}

  public List<Service> getServices() {
    return services;
  }

  public JsonObject getAsJson() {
    JsonObject message = new JsonObject();
    message.addProperty("key", Cloud.key);
    message.addProperty("type", type.name());
    JsonArray data = new JsonArray();
    for (Service service : services) {
      if (service.getServiceChannel() == null) continue;
      if (!service.isProcessAlive()) continue;
      JsonObject subData = new JsonObject();
      subData.addProperty("name", service.getServiceName());
      subData.addProperty("ip", Cloud.server.getIp());
      subData.addProperty("port", service.getServicePort());
      subData.addProperty("state", service.getServiceState().name());
      data.add(subData);
    }
    message.add("data", data);
    return message;
  }

  /**
   * This method gets the full packet message from {@link JsonObject}.
   *
   * @return the packet message
   */
  public String message() {
    return getAsJson().toString();
  }

  /**
   * This method adds a service list to the packet.
   *
   * @param services the list of services
   */
  public void s(List<Service> services) {
    this.services = services;
  }
}
