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

package de.cloud.api.delta203.core;

import de.cloud.api.delta203.core.utils.CloudServiceState;

public class CloudService {

  private final String name;
  private final String ip;
  private final int port;

  private CloudServiceState state;

  public CloudService(String name, String ip, int port, CloudServiceState state) {
    this.name = name;
    this.ip = ip;
    this.port = port;
    this.state = state;
  }

  public String getName() {
    return name;
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  public CloudServiceState getState() {
    return state;
  }

  public void setState(CloudServiceState state) {
    this.state = state;
  }
}
