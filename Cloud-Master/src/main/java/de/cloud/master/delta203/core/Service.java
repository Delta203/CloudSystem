package de.cloud.master.delta203.core;

import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.main.Cloud;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Service extends Thread {

  public boolean debug = false;

  private final Group group;
  private final String path;

  private String name;
  private int port;
  private Process process;

  public Service(Group group) {
    this.group = group;
    setName();
    setPort();
    if (group.isStatic()) path = Cloud.pathManager.getPathServicesStatic() + "/" + name;
    else path = Cloud.pathManager.getPathServicesTemp() + "/" + name;
  }

  /*
   * Initialise service data
   */

  private boolean nameExists(String name) {
    for (Service service : Cloud.services) {
      if (service.name.equals(name)) return true;
    }
    return false;
  }

  private void setName() {
    int id = 1;
    name = group.getName() + "-" + id;
    while (nameExists(name)) {
      id++;
      name = group.getName() + "-" + id;
    }
  }

  private boolean portExists(int port) {
    for (Service service : Cloud.services) {
      if (service.port == port) return true;
    }
    return false;
  }

  private void setPort() {
    port = Constants.Locals.DEFAULT_PORT;
    if (group.getType() == GroupType.SERVER) port = Constants.Locals.START_PORT;
    while (portExists(port)) port++;
  }

  public String getServiceName() {
    return name;
  }

  /*
   * File functions
   */

  private List<String> readFile(File file) {
    List<String> data = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) data.add(line);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return data;
  }

  private void writeFile(File file, List<String> data) {
    try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, false))) {
      for (String s : data) writer.println(s);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private void copyDefault() {
    String from =
        group.getType() == GroupType.PROXY
            ? Cloud.pathManager.getPathTemplatesDefaultProxy()
            : Cloud.pathManager.getPathTemplatesDefaultServer();
    Cloud.pathManager.copyDirectory(new File(from).toPath(), new File(path).toPath());
  }

  private void copyTemplate() {
    String from = Cloud.pathManager.getPathTemplates() + "/" + group.getName();
    Cloud.pathManager.copyDirectory(new File(from).toPath(), new File(path).toPath());
  }

  private void addEula() {
    if (group.getType() != GroupType.SERVER) return;
    List<String> data = new ArrayList<>();
    data.add("# Cloud System generated file");
    data.add(
        "# By changing the setting below to TRUE you are indicating your agreement to our EULA (https://aka.ms/MinecraftEULA).");
    data.add("eula=true");
    writeFile(new File(path + "/eula.txt"), data);
  }

  private void modifyProxy() {
    if (group.getType() != GroupType.PROXY) return;
    List<String> data = new ArrayList<>();
    data.add("# Cloud System generated file");
    File config = new File(path + "/config.yml");
    if (config.exists()) {
      // read config data
      for (String s : readFile(config)) {
        if (s.startsWith("  host:")) data.add("  host: " + Cloud.server.getIp() + ":" + port);
        else data.add(s);
      }
    } else {
      // add default
      data.add("ip_forward: true");
      data.add("listeners:");
      data.add("- query_port: 25577");
      data.add("  query_enabled: false");
      data.add("  bind_local_address: true");
      data.add("  host: " + Cloud.server.getIp() + ":" + port);
    }
    writeFile(config, data);
  }

  private void modifyServer() {
    if (group.getType() != GroupType.SERVER) return;
    List<String> dataProperties = new ArrayList<>();
    dataProperties.add("# Cloud System generated file");
    List<String> dataSpigot = new ArrayList<>();
    dataSpigot.add("# Cloud System generated file");
    File properties = new File(path + "/server.properties");
    File spigot = new File(path + "/spigot.yml");
    if (properties.exists()) {
      // read properties data
      for (String s : readFile(properties)) {
        if (s.startsWith("server-port=")) dataProperties.add("server-port=" + port);
        else dataProperties.add(s);
      }
    } else {
      // add default
      dataProperties.add("server-port=" + port);
      dataProperties.add("online-mode=false");
    }
    writeFile(properties, dataProperties);
    if (spigot.exists()) {
      // read spigot data
      for (String s : readFile(spigot)) {
        if (s.startsWith("  bungeecord:")) dataSpigot.add("  bungeecord: true");
        else dataSpigot.add(s);
      }
    } else {
      // add default
      dataSpigot.add("settings:");
      dataSpigot.add("  bungeecord: true");
    }
    writeFile(spigot, dataSpigot);
  }

  private void addCloudAPI() {
    // copy api file
    File plugins = new File(path + "/plugins");
    if (!plugins.exists()) plugins.mkdirs();
    Cloud.pathManager.copyFile(
        new File(Cloud.pathManager.getPathAssetsAPI() + "/" + Constants.Locals.API).toPath(),
        new File(plugins + "/" + Constants.Locals.API).toPath());
    // insert config with server data
    File config = new File(path + "/plugins/Cloud-API");
    if (!config.exists()) config.mkdirs();
    List<String> data = new ArrayList<>();
    data.add("# Cloud System generated file");
    data.add("name: " + name);
    data.add("server:");
    data.add("  ip: " + Cloud.server.getIp());
    data.add("  port: " + Cloud.server.getPort());
    data.add("  key: " + Cloud.key);
    writeFile(new File(config.getPath() + "/config.yml"), data);
  }

  /**
   * This method copies all necessary data into the respective service directory. The essential data
   * are already initialised when {@link Service} is created.
   */
  public boolean register() {
    if (Cloud.config.getData().get("maxMemory").getAsInt() - (Cloud.memory + group.getMemory())
        < 0) {
      Cloud.console.print("§c" + name + " not enough memory!", "§3Service§r");
      return false;
    }
    copyDefault();
    copyTemplate();
    addEula();
    modifyProxy();
    modifyServer();
    addCloudAPI();
    Cloud.services.add(this);
    Cloud.memory += group.getMemory();
    Cloud.console.print(name + " files loaded...", "§3Service§r");
    return true;
  }

  /*
   * Process functions
   */

  private ProcessBuilder buildProcess() {
    String[] command =
        Constants.Locals.START_WIN
            .replace("%memory%", String.valueOf(group.getMemory()))
            .replace("%file%", group.getType().version)
            .split(" ");
    ProcessBuilder builder = new ProcessBuilder(command);
    builder.directory(new File(path));
    return builder;
  }

  @Override
  public void run() {
    ProcessBuilder builder = buildProcess();
    try {
      process = builder.start();
      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          if (debug) System.out.println(line);
        }
      }
      process.waitFor();
      int exitCode = process.exitValue();
      Cloud.console.print(name + " exited with code: " + exitCode, "§3Service§r");
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void stopProcess() {
    if (process == null || !process.isAlive()) return;
    process.destroyForcibly();
  }
}
