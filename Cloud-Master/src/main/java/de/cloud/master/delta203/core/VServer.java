package de.cloud.master.delta203.core;

import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.main.Cloud;
import java.io.*;

public class VServer {

  private final Group group;
  private Process process;

  private String name;
  private final String pathTarget;

  public VServer(Group group) {
    this.group = group;
    pathTarget = Cloud.pathManager.getPathServicesTemp();
    setName();
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

  public Group getGroup() {
    return group;
  }

  public String getName() {
    return name;
  }

  private void mkdir() {
    new File(pathTarget + "/" + name).mkdirs();
  }

  private void copyDefaults() {
    String defaults =
        group.getType() == GroupType.PROXY
            ? Cloud.pathManager.getPathTemplatesDefaultProxy()
            : Cloud.pathManager.getPathTemplatesDefaultServer();
    File from = new File(defaults);
    File to = new File(pathTarget + "/" + name);
    Cloud.pathManager.copyDirectory(from.toPath(), to.toPath());
  }

  public void copyFiles() {
    if (group.isStatic()) return;
    mkdir();
    copyDefaults();
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

  private void addConfig() {
    try (PrintWriter writer =
        new PrintWriter(new FileOutputStream(pathTarget + "/" + name + "/config.yml", false))) {
      writer.println("# Cloud System generated file");
      writer.println("connection_throttle: 0");
      writer.println("ip_forward: true");
      writer.println("listeners:");
      writer.println("- query_port: 25565");
      writer.println("  query_enabled: false");
      writer.println("  bind_local_address: true");
      writer.println("  host: 0.0.0.0:25565");
      writer.println("groups:");
      writer.println("  Delta203:");
      writer.println("  - admin");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void addFiles() {
    if (group.getType() == GroupType.PROXY) addConfig();
    else addEula();
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
              try {
                process = processBuilder.start();
                process.waitFor();
                int exitCode = process.exitValue();
                Cloud.console.print(name + " exited with code: " + exitCode, "§3Service§r");
                unregister();
              } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
              }
            })
        .start();
  }

  public void stopProcess() {
    if (process == null || !process.isAlive()) return;
    process.destroyForcibly();
  }
}
