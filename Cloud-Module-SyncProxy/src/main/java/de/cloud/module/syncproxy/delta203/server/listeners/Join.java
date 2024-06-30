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

package de.cloud.module.syncproxy.delta203.server.listeners;

import de.cloud.module.syncproxy.delta203.server.SyncProxy;
import de.cloud.module.syncproxy.delta203.server.TabManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    TabManager tabManager = new TabManager(e.getPlayer());
    tabManager.runTaskTimer(SyncProxy.plugin, 0, tabManager.getUpdate());
  }
}
