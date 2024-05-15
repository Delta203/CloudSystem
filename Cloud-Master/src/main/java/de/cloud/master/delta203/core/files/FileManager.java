package de.cloud.master.delta203.core.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class FileManager {

  private final String path;
  private final String name;
  private final File file;
  private JsonObject data;

  public FileManager(String path, String name) {
    this.path = path;
    this.name = name;
    file = new File(path, name);
  }

  public String getPath() {
    return path;
  }

  public String getName() {
    return name;
  }

  public File getFile() {
    return file;
  }

  public void load() {
    if (!file.exists()) return;
    try {
      data = (JsonObject) JsonParser.parseReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void save() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(data);
    try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, false))) {
      writer.println(json);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public JsonObject getData() {
    return data;
  }

  public void setData(JsonObject data) {
    this.data = data;
  }
}
