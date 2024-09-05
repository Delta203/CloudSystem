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

package de.cloud.module.antivpn.delta203.proxy;

import de.cloud.module.antivpn.delta203.proxy.files.FileManager;
import de.cloud.module.antivpn.delta203.proxy.listeners.PreLogin;
import de.cloud.module.antivpn.delta203.proxy.utils.VPNHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class AntiVPN extends Plugin {

  public static AntiVPN plugin;
  public static Configuration config;
  public static VPNHandler vpnHandler;

  @Override
  public void onEnable() {
    plugin = this;
    loadConfig();
    vpnHandler = new VPNHandler();

    ProxyServer.getInstance().getPluginManager().registerListener(plugin, new PreLogin());
  }

  private void loadConfig() {
    FileManager configYml = new FileManager("config.yml");
    configYml.create();
    configYml.load();
    config = configYml.get();
  }
}
