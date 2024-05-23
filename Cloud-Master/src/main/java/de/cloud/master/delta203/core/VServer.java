package de.cloud.master.delta203.core;

import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.main.Cloud;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VServer {

  private final Group group;
  private Process process;

  private String name;
  private int port = 25565;
  private final String pathTarget;

  public VServer(Group group) {
    this.group = group;
    pathTarget = Cloud.pathManager.getPathServicesTemp();
    setName();
    setPort();
  }

  private boolean nameExists(String name) {
    for (VServer server : Cloud.services) {
      if (server.name.equals(name)) return true;
    }
    return false;
  }

  private void setName() {
    int id = 1;
    String name = group.getName() + "-" + id;
    while (nameExists(name)) {
      name = group.getName() + "-" + id;
      id++;
    }
    this.name = name;
  }

  private boolean portExists(int port) {
    for (VServer server : Cloud.services) {
      if (server.port == port) return true;
    }
    return false;
  }

  private void setPort() {
    if (group.getType() == GroupType.PROXY) {
      port = Constants.Locals.DEFAULT_PORT;
      return;
    }
    int i = Constants.Locals.START_PORT;
    while (portExists(i)) {
      i++;
    }
    port = i;
  }

  public Group getGroup() {
    return group;
  }

  public String getName() {
    return name;
  }

  private void mkdir() {
    new File(pathTarget + "/" + name).mkdirs();
  }

  private void copyDefault() {
    String defaults =
        group.getType() == GroupType.PROXY
            ? Cloud.pathManager.getPathTemplatesDefaultProxy()
            : Cloud.pathManager.getPathTemplatesDefaultServer();
    File from = new File(defaults);
    File to = new File(pathTarget + "/" + name);
    Cloud.pathManager.copyDirectory(from.toPath(), to.toPath());
  }

  private void copyTemplate() {
    File from = new File(Cloud.pathManager.getPathTemplates() + "/" + group.getName());
    File to = new File(pathTarget + "/" + name);
    Cloud.pathManager.copyDirectory(from.toPath(), to.toPath());
  }

  private void addEula() {
    try (PrintWriter writer =
        new PrintWriter(new FileOutputStream(pathTarget + "/" + name + "/eula.txt", false))) {
      writer.println("# Cloud System generated file");
      writer.println(
          "#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://aka.ms/MinecraftEULA).");
      writer.println("eula=true");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void copyFiles() {
    if (group.isStatic()) return;
    mkdir();
    copyDefault();
    copyTemplate();
    if (group.getType() == GroupType.SERVER) addEula();
  }

  private void changePortProxy() {
    String file = pathTarget + "/" + name + "/" + "config.yml";
    List<String> data = new ArrayList<>();
    if (new File(file).exists()) {
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.startsWith("  host:")) data.add("  host: " + Cloud.server.getIp() + ":" + port);
          else data.add(line);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      data.add("ip_forward: true");
      data.add("listeners:");
      data.add("- query_port: 25577");
      data.add("  query_enabled: false");
      data.add("  bind_local_address: true");
      data.add("  host: " + Cloud.server.getIp() + ":" + port);
    }
    try (PrintWriter writer =
        new PrintWriter(new FileOutputStream(pathTarget + "/" + name + "/config.yml", false))) {
      for (String s : data) writer.println(s);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private void changePortServer() {}

  public void changePort() {
    if (group.getType() == GroupType.PROXY) changePortProxy();
    else changePortServer();
  }

  public void register() {
    Cloud.services.add(this);
    Cloud.memory += group.getMemory();
  }

  public void unregister() {
    if (Cloud.shutdown) return;
    Cloud.services.remove(this);
    Cloud.memory -= group.getMemory();
  }

  public void runProcess() {
    Cloud.console.print(name + " files loaded and currently starting...", "§3Service§r");
    String[] command =
        Constants.Locals.START_WIN
            .replace("%memory%", String.valueOf(group.getMemory()))
            .replace("%file%", group.getType().version)
            .split(" ");
    ProcessBuilder processBuilder = new ProcessBuilder(command);
    processBuilder.directory(new File(pathTarget + "/" + name));
    // processBuilder.inheritIO();

    new Thread(
            () -> {
              System.out.println("TODO: Run VServers...");
              /*
              try {
                process = processBuilder.start();
                process.waitFor();
                int exitCode = process.exitValue();
                Cloud.console.print(name + " exited with code: " + exitCode, "§3Service§r");
                unregister();
              } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
              }
              */
            })
        .start();
  }

  public void stopProcess() {
    if (process == null || !process.isAlive()) return;
    process.destroyForcibly();
  }
}
