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

package de.cloud.master.delta203.core;

import de.cloud.master.delta203.core.packets.PacketAddServer;
import de.cloud.master.delta203.core.packets.PacketCommand;
import de.cloud.master.delta203.core.packets.PacketServiceInfo;
import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.core.utils.OSType;
import de.cloud.master.delta203.core.utils.ServiceState;
import de.cloud.master.delta203.main.Cloud;
import de.cloud.master.delta203.main.sockets.Channel;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Service extends Thread {

  public boolean debug = false;

  private final Group group;
  private final String path;

  private String name;
  private int port;
  private ServiceState state;

  private Channel channel;
  private Process process;

  public Service(Group group) {
    this.group = group;
    setName();
    setPort();
    state = group.getType() == GroupType.PROXY ? ServiceState.PROXY : ServiceState.LOBBY;
    channel = null;
    if (group.isStatic()) path = Cloud.pathManager.getPathServicesStatic() + "/" + name;
    else path = Cloud.pathManager.getPathServicesTemp() + "/" + name;
  }

  /*
   * Initialise service data
   */

  private void setName() {
    if (group.isStatic()) {
      name = group.getName();
      return;
    }
    int id = 1;
    name = group.getName() + "-" + id;
    while (Cloud.services.containsKey(name)) {
      id++;
      name = group.getName() + "-" + id;
    }
  }

  private boolean portExists(int port) {
    for (Service service : Cloud.services.values()) {
      if (service.port == port) return true;
    }
    return false;
  }

  private int generateRandomPort() {
    Random rdm = new Random();
    return rdm.nextInt((Constants.Locals.MAX_PORT - Constants.Locals.START_PORT) + 1)
        + Constants.Locals.START_PORT;
  }

  private void setPort() {
    port = Constants.Locals.DEFAULT_PORT;
    if (group.getType() == GroupType.SERVER) port = generateRandomPort();
    while (portExists(port)) port = generateRandomPort();
  }

  public Group getServiceGroup() {
    return group;
  }

  public String getServiceName() {
    return name;
  }

  public int getServicePort() {
    return port;
  }

  public ServiceState getServiceState() {
    return state;
  }

  public Channel getServiceChannel() {
    return channel;
  }

  public boolean isProcessAlive() {
    return process != null && process.isAlive();
  }

  public void setServiceInGame() {
    state = ServiceState.INGAME;
    // broadcast service infos
    PacketServiceInfo serviceInfo = new PacketServiceInfo();
    serviceInfo.s(new ArrayList<>(Cloud.services.values()));
    channel.broadcast(serviceInfo.message(), false);
    // try to start new services
    group.runServices();
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
    data.add("# Cloud-System generated file");
    data.add(
        "# By changing the setting below to TRUE you are indicating your agreement to our EULA (https://aka.ms/MinecraftEULA).");
    data.add("eula=true");
    writeFile(new File(path + "/eula.txt"), data);
  }

  private void modifyProxy() {
    if (group.getType() != GroupType.PROXY) return;
    List<String> data = new ArrayList<>();
    data.add("# Cloud-System generated file");
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
      data.add("log_pings: false");
      data.add("log_commands: false");
      data.add("listeners:");
      data.add("- query_port: 25577");
      data.add("  query_enabled: false");
      data.add("  bind_local_address: true");
      data.add("  host: " + Cloud.server.getIp() + ":" + port);
      data.add("  force_default_server: true");
    }
    writeFile(config, data);
  }

  private void modifyServer() {
    if (group.getType() != GroupType.SERVER) return;
    List<String> dataProperties = new ArrayList<>();
    dataProperties.add("# Cloud-System generated file");
    List<String> dataSpigot = new ArrayList<>();
    dataSpigot.add("# Cloud-System generated file");
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
    data.add("# Cloud-System generated file");
    data.add("name: " + name);
    data.add("server:");
    data.add("  ip: " + Cloud.server.getIp());
    data.add("  port: " + Cloud.server.getPort());
    data.add("  key: " + Cloud.key);
    writeFile(new File(config.getPath() + "/config.yml"), data);
  }

  private void addCloudModules() {
    // copy modules
    File plugins = new File(path + "/plugins");
    if (!plugins.exists()) plugins.mkdirs();
    String from = Cloud.pathManager.getPathModules();
    Cloud.pathManager.copyDirectory(new File(from).toPath(), plugins.toPath());
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
    Cloud.services.put(name, this);
    Cloud.memory += group.getMemory();

    if (!group.isStatic()) {
      copyDefault();
      copyTemplate();
    }
    addEula();
    modifyProxy();
    modifyServer();
    addCloudAPI();
    addCloudModules();
    Cloud.console.print(name + " files loaded...", "§3Service§r");
    return true;
  }

  public boolean filesRegistered() {
    return (group.getType() == GroupType.PROXY
        ? new File(path + "/config.yml").exists()
        : new File(path + "/server.properties").exists()
            && new File(path + "/spigot.yml").exists()
            && new File(path + "/eula.txt").exists());
  }

  /**
   * This method registers a channel as soon as it has connected to the socket {@link
   * de.cloud.master.delta203.main.sockets.Server}.
   */
  public void registerChannel(Channel channel) {
    this.channel = channel;
    Cloud.console.print(name + ":" + port + " successfully connected.", "§bChannel§r");
    if (group.getType() == GroupType.PROXY) {
      // broadcast online channels to new proxy
      for (Service connected : Cloud.services.values()) {
        if (connected.group.getType() != GroupType.SERVER) continue;
        if (connected.channel == null) continue;
        PacketAddServer addServer = new PacketAddServer();
        addServer.n(connected.getServiceName());
        addServer.i(Cloud.server.getIp());
        addServer.p(connected.getServicePort());
        connected.channel.broadcast(addServer.message(), true);
      }
    } else {
      // broadcast new service to proxy
      PacketAddServer addServer = new PacketAddServer();
      addServer.n(name);
      addServer.i(Cloud.server.getIp());
      addServer.p(port);
      channel.broadcast(addServer.message(), true);
    }
    // broadcast service infos
    PacketServiceInfo serviceInfo = new PacketServiceInfo();
    serviceInfo.s(new ArrayList<>(Cloud.services.values()));
    channel.broadcast(serviceInfo.message(), false);
  }

  /*
   * Process functions
   */

  private ProcessBuilder buildProcess() {
    String[] command =
        Constants.Locals.START_LINUX
            .replace("%memory%", String.valueOf(group.getMemory()))
            .replace("%file%", group.getType().version)
            .split(" ");
    if (Cloud.os == OSType.WINDOWS) {
      command =
          Constants.Locals.START_WINDOWS
              .replace("%memory%", String.valueOf(group.getMemory()))
              .replace("%file%", group.getType().version)
              .split(" ");
    }
    ProcessBuilder builder = new ProcessBuilder(command);
    builder.directory(new File(path));
    return builder;
  }

  @Override
  public void run() {
    ProcessBuilder builder = buildProcess();
    try {
      process = builder.start();
      // run console in background
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
    } catch (IOException | InterruptedException ignored) {
    } finally {
      unregister();
    }
  }

  private void unregister() {
    if (Cloud.shutdown) return;
    if (!group.isStatic()) Cloud.pathManager.deleteDirectory(Paths.get(path));
    Cloud.services.remove(name);
    Cloud.memory -= group.getMemory();
    // restart services if needed
    while (new File(path).exists()) {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    group.runServices();
  }

  public void stopProcess() {
    if (!isProcessAlive()) return;
    if (channel != null) {
      String command = group.getType() == GroupType.PROXY ? "end" : "stop";
      PacketCommand packetCommand = new PacketCommand();
      packetCommand.c(command);
      channel.sendMessage(packetCommand.message());
    } else {
      process.destroy();
    }
  }
}
