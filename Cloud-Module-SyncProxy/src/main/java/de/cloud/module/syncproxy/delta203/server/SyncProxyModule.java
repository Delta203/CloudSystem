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

package de.cloud.module.syncproxy.delta203.server;

import de.cloud.module.syncproxy.delta203.server.files.FileManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class SyncProxyModule extends JavaPlugin {

  public static SyncProxyModule plugin;

  public static Configuration config;
  public static Configuration database;


  @Override
  public void onEnable() {
    plugin = this;
  }

  private void registerConfigs() {
    // load config
    FileManager configManager = new FileManager("config.yml");
    configManager.create();
    configManager.load();
    config = configManager.get();
    // load sign database
    FileManager databaseManager = new FileManager("database.yml");
    databaseManager.create();
    databaseManager.load();
    database = databaseManager.get();
  }
}
