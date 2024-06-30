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

import java.time.LocalDate;
import java.time.LocalTime;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TabManager extends BukkitRunnable {

  private final Player player;
  private final int update;
  private String header;
  private String footer;

  public TabManager(Player player) {
    this.player = player;
    update = SyncProxy.config.getInt("tabList.update");
    header = SyncProxy.config.getString("tabList.header");
    footer = SyncProxy.config.getString("tabList.footer");
  }

  private String getDate() {
    return LocalDate.now().toString();
  }

  private String getTime() {
    return LocalTime.now().toString();
  }

  public int getUpdate() {
    return update;
  }

  private void tabList() {
    player.setPlayerListHeaderFooter(header, footer);
  }

  @Override
  public void run() {
    header = header.replace("\\n", "\n").replace("%date%", getDate()).replace("%time%", getTime());
    footer = footer.replace("\\n", "\n").replace("%date%", getDate()).replace("%time%", getTime());
    tabList();
  }
}
