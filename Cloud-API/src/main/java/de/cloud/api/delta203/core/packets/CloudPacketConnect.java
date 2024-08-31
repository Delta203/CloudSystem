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

package de.cloud.api.delta203.core.packets;

import com.google.gson.JsonObject;
import de.cloud.api.delta203.core.utils.CloudMessageType;

import java.util.Objects;

/** This class is a cloud packet. It is sent to connect to the server socket. */
public class CloudPacketConnect {

  public static final CloudMessageType type = CloudMessageType.CONNECT;

  private String key;
  private String name;

  public CloudPacketConnect() {}

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public JsonObject getAsJson() {
    JsonObject message = new JsonObject();
    message.addProperty("key", Objects.requireNonNull(key));
    message.addProperty("type", type.name());
    JsonObject data = new JsonObject();
    data.addProperty("name", Objects.requireNonNull(name));
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
   * This method adds a key to the packet.
   *
   * @param key the communication key
   */
  public void k(String key) {
    this.key = key;
  }

  /**
   * This method adds the service name to the packet.
   *
   * @param name the name of service
   */
  public void n(String name) {
    this.name = name;
  }
}
