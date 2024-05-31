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

import com.google.gson.JsonObject;
import de.cloud.master.delta203.core.files.Downloader;
import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.core.utils.OSType;
import de.cloud.master.delta203.main.Application;
import de.cloud.master.delta203.main.Cloud;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Setup {

  private final JsonObject config;

  public Setup() {
    config = new JsonObject();
  }

  private String os() {
    Cloud.console.print(
        "On which system is the cloud running? [" + OSType.LINUX + ", " + OSType.WINDOWS + "]");
    String os = Application.scanner.nextLine();
    while (!os.equals(OSType.LINUX.name()) && !os.equals(OSType.WINDOWS.name())) {
      Cloud.console.print("The specified os version is invalid.");
      os = Application.scanner.nextLine();
    }
    return os;
  }

  private Object[] address() {
    Cloud.console.print("On which address should the server run?");
    String ip = "localhost";
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException ignored) {
    }
    Cloud.console.print("Press enter for default value: " + ip + ":" + 1550);
    String input = Application.scanner.nextLine();
    if (input.isEmpty()) return new Object[] {ip, 1550};
    while (!input.contains(":")) {
      Cloud.console.print("You must enter a valid address!");
      input = Application.scanner.nextLine();
    }
    String[] address = input.split(":");
    return new Object[] {address[0], Integer.decode(address[1])};
  }

  private int memory() {
    Cloud.console.print("How much memory should the cloud have in total? (MB)");
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

  private String proxy() {
    Cloud.console.print(
        "What kind of proxy version do you want to use? " + Constants.Links.PROXIES.keySet());
    String proxy = Application.scanner.nextLine();
    while (!Constants.Links.PROXIES.containsKey(proxy)) {
      Cloud.console.print("The specified proxy version is invalid.");
      proxy = Application.scanner.nextLine();
    }
    try {
      new Downloader(
              Constants.Links.PROXIES.get(proxy),
              Cloud.pathManager.getPathAssetsProxy(),
              proxy + ".jar")
          .download();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return proxy + ".jar";
  }

  private String server() {
    Cloud.console.print(
        "What kind of server version do you want to use? " + Constants.Links.SERVERS.keySet());
    String server = Application.scanner.nextLine();
    while (!Constants.Links.SERVERS.containsKey(server)) {
      Cloud.console.print("The specified server version is invalid.");
      server = Application.scanner.nextLine();
    }
    try {
      new Downloader(
              Constants.Links.SERVERS.get(server),
              Cloud.pathManager.getPathAssetsServer(),
              server + ".jar")
          .download();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return server + ".jar";
  }

  public void run() {
    Cloud.console.print("The setup has started:");
    config.addProperty("os", os());
    Object[] address = address();
    config.addProperty("ip", (String) address[0]);
    config.addProperty("port", (int) address[1]);
    config.addProperty("maxMemory", memory());
    JsonObject versions = new JsonObject();
    versions.addProperty("proxy", proxy());
    versions.addProperty("server", server());
    config.add("versions", versions);
    Cloud.config.setData(config);
    Cloud.config.save();
    Cloud.console.print("Configurations are saved...");
    Cloud.console.print("§2The installation has been completed!");
    Cloud.console.clear();
  }
}
