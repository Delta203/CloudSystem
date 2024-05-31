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

package de.cloud.master.delta203.core.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class FileManager {

  private final String name;
  private final File file;
  private JsonObject data;

  public FileManager(String path, String name) {
    this.name = name;
    file = new File(path, name);
  }

  public String getName() {
    return name;
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
