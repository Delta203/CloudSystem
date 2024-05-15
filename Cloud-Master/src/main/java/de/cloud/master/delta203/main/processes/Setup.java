package de.cloud.master.delta203.main.processes;

import com.google.gson.JsonObject;
import de.cloud.master.delta203.core.files.Downloader;
import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.main.Application;
import de.cloud.master.delta203.main.Cloud;

import java.io.IOException;

public class Setup {

  private final JsonObject config;

  public Setup() {
    config = new JsonObject();
  }

  private String ip() {
    Cloud.console.print("On which ip should the server run?");
    return Application.scanner.nextLine();
  }

  private int port() {
    Cloud.console.print("On which port should the server run? [1550]");
    int port = 0;
    while (port == 0) {
      try {
        port = Integer.decode(Application.scanner.nextLine());
      } catch (NumberFormatException e) {
        Cloud.console.print("You must enter a number!");
      }
    }
    return port;
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
    config.addProperty("ip", ip());
    config.addProperty("port", port());
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
