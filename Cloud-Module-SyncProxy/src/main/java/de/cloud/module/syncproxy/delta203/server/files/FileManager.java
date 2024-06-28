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

package de.cloud.module.syncproxy.delta203.server.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import de.cloud.module.syncproxy.delta203.server.SyncProxyModule;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * <b>File Manager</b><br>
 * This is content from Spigot Utils <small>(Useful classes for making a spigot plugin)</small>.
 *
 * @see <a href="https://github.com/Delta203/SpigotUtils">Spigot Utils</a>
 * @author Delta203
 * @version 1.1
 */
public class FileManager {

  private final String filename;
  private final File file;
  private FileConfiguration cfg;

  /**
   * Register a FileManager with the specified filename to handle a configuration file.
   *
   * @param filename the name of the config file
   */
  public FileManager(String filename) {
    this.filename = filename;
    file = new File(SyncProxyModule.plugin.getDataFolder(), filename);
  }

  /**
   * Create the config file if it does not exist. If the plugins data folder does not exist, create
   * it as well.
   */
  public void create() {
    if (!SyncProxyModule.plugin.getDataFolder().exists()) {
      SyncProxyModule.plugin.getDataFolder().mkdir();
    }
    try {
      if (!file.exists()) {
        Files.copy(
            Objects.requireNonNull(SyncProxyModule.plugin.getResource(filename)), file.toPath());
      }
      cfg = YamlConfiguration.loadConfiguration(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Loads the configuration from the file. */
  public void load() {
    try {
      cfg.load(file);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  /** Saves the configuration to the file. */
  public void save() {
    try {
      cfg.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the configuration object.
   *
   * @return the file configuration
   */
  public Configuration get() {
    return cfg;
  }
}
