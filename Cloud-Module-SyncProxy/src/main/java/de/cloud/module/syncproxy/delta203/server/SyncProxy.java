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
import de.cloud.module.syncproxy.delta203.server.utils.PlayerCount;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class SyncProxy extends JavaPlugin {

  public static SyncProxy plugin;
  public static Configuration config;

  @Override
  public void onEnable() {
    plugin = this;
    Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", new PlayerCount());

    loadConfig();

    TabManager tabManager = new TabManager();
    tabManager.runTaskTimer(plugin, 0, tabManager.getUpdate());
  }

  @Override
  public void onDisable() {
    Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin);
    Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin);
  }

  private void loadConfig() {
    FileManager configYml = new FileManager("config.yml");
    configYml.create();
    configYml.load();
    config = configYml.get();
  }
}
