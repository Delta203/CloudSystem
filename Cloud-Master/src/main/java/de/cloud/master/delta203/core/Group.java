package de.cloud.master.delta203.core;

import com.google.gson.JsonObject;
import de.cloud.master.delta203.core.files.FileManager;
import de.cloud.master.delta203.core.utils.GroupType;
import de.cloud.master.delta203.main.Cloud;
import java.io.File;

public class Group {

  private String name;
  private GroupType type;
  private int memory;
  private int minAmount;
  private int maxAmount;
  private boolean statisch;

  private final FileManager data;

  public Group(
      String name, GroupType type, int memory, int minAmount, int maxAmount, boolean statisch) {
    this.name = name;
    this.type = type;
    this.memory = memory;
    this.minAmount = minAmount;
    this.maxAmount = maxAmount;
    this.statisch = statisch;
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
    minAmount = content.get("minAmount").getAsInt();
    maxAmount = content.get("maxAmount").getAsInt();
    statisch = content.get("static").getAsBoolean();
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

  public int getMinAmount() {
    return minAmount;
  }

  public int getMaxAmount() {
    return maxAmount;
  }

  public boolean isStatic() {
    return statisch;
  }

  private void mkdir() {
    String target =
        statisch ? Cloud.pathManager.getPathServicesStatic() : Cloud.pathManager.getPathTemplates();
    new File(target + "/" + name).mkdirs();
  }

  private void copyFiles() {
    String assets =
        type == GroupType.PROXY
            ? Cloud.pathManager.getPathAssetsProxy()
            : Cloud.pathManager.getPathAssetsServer();
    String target =
        statisch ? Cloud.pathManager.getPathServicesStatic() : Cloud.pathManager.getPathTemplates();
    File from = new File(assets + "/" + type.version);
    File to = new File(target + "/" + name + "/" + type.version);
    Cloud.pathManager.copyFile(from.toPath(), to.toPath());
  }

  public void create() {
    JsonObject content = new JsonObject();
    content.addProperty("name", name);
    content.addProperty("type", type.name());
    content.addProperty("memory", memory);
    content.addProperty("minAmount", minAmount);
    content.addProperty("maxAmount", maxAmount);
    content.addProperty("static", statisch);
    data.setData(content);
    data.save();
    mkdir();
    copyFiles();
  }

  public void runServices() {
    int online = 0;
    for (Service service : Cloud.services.values()) {
      if (service.getServiceGroup().equals(this)) online++;
    }
    for (int i = 0; (i < minAmount - online) && i < maxAmount; i++) {
      Service service = new Service(this);
      if (service.register()) service.start();
    }
  }
}
