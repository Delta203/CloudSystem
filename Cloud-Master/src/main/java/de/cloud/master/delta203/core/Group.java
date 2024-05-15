package de.cloud.master.delta203.core;

import com.google.gson.JsonObject;
import de.cloud.master.delta203.core.files.FileManager;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.main.Cloud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Group {

  private String name;
  private GroupType type;
  private int memory;
  private int amount;
  private boolean statisch;
  private boolean maintenance;

  private final FileManager data;

  public Group(
      String name, GroupType type, int memory, int amount, boolean statisch, boolean maintenance) {
    this.name = name;
    this.type = type;
    this.memory = memory;
    this.amount = amount;
    this.statisch = statisch;
    this.maintenance = maintenance;
    data = new FileManager(Cloud.pathManager.getPathDataGroup(), name + ".json");
  }

  public Group(FileManager data) {
    this.data = data;
  }

  public void load() {
    data.load();
    JsonObject content = data.getData();
    name = content.get("name").getAsString();
    type = GroupType.valueOf(content.get("type").getAsString());
    memory = content.get("memory").getAsInt();
    amount = content.get("amount").getAsInt();
    statisch = content.get("static").getAsBoolean();
    maintenance = content.get("maintenance").getAsBoolean();
  }

  public String getName() {
    return name;
  }

  public GroupType getType() {
    return type;
  }

  public int getMemory() {
    return memory;
  }

  public int getAmount() {
    return amount;
  }

  public boolean isStatic() {
    return statisch;
  }

  public boolean isMaintenance() {
    return maintenance;
  }

  private void mkdir() {
    new File(Cloud.pathManager.getPathTemplates() + "/" + name).mkdirs();
  }

  private void copyFiles() {
    String file;
    Path from;
    Path to;
    if (type == GroupType.PROXY) {
      file = Cloud.config.getData().get("versions").getAsJsonObject().get("proxy").getAsString();
      from = Paths.get(Cloud.pathManager.getPathAssetsProxy() + "/" + file);
    } else {
      file = Cloud.config.getData().get("versions").getAsJsonObject().get("server").getAsString();
      from = Paths.get(Cloud.pathManager.getPathAssetsServer() + "/" + file);
    }
    to = Paths.get(Cloud.pathManager.getPathTemplates() + "/" + name + "/" + file);
    try {
      Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void create() {
    JsonObject content = new JsonObject();
    content.addProperty("name", name);
    content.addProperty("type", type.name());
    content.addProperty("memory", memory);
    content.addProperty("amount", amount);
    content.addProperty("static", statisch);
    content.addProperty("maintenance", maintenance);
    data.setData(content);
    data.save();
    mkdir();
    copyFiles();
  }
}
