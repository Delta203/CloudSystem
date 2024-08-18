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

import de.cloud.api.delta203.core.CloudInstance;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.cloud.module.syncproxy.delta203.server.utils.PlayerCount;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TabManager extends BukkitRunnable {

  private final int update;
  private final String header;
  private final String footer;

  public TabManager() {
    update = SyncProxy.config.getInt("tabList.update");
    header = SyncProxy.config.getString("tabList.header");
    footer = SyncProxy.config.getString("tabList.footer");
  }

  public int getUpdate() {
    return update;
  }

  private String getHeader(Player player) {
    return header
        .replace("\\n", "\n")
        .replace("%service%", CloudInstance.name)
        .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
        .replace("%onlineP%", String.valueOf(PlayerCount.getPlayerCount()))
        .replace("%max%", String.valueOf(Bukkit.getMaxPlayers()))
        .replace("%ping%", String.valueOf(getPing(player)))
        .replace("%time%", getTime());
  }

  private String getFooter(Player player) {
    return footer
        .replace("\\n", "\n")
        .replace("%service%", CloudInstance.name)
        .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
        .replace("%onlineP%", String.valueOf(PlayerCount.getPlayerCount()))
        .replace("%max%", String.valueOf(Bukkit.getMaxPlayers()))
        .replace("%ping%", String.valueOf(getPing(player)))
        .replace("%time%", getTime());
  }

  private int getPing(Player player) {
    return player.getPing();
  }

  private String getTime() {
    return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
  }

  @Override
  public void run() {
    List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
    if (!players.isEmpty()) PlayerCount.flushPlayerCount(players.get(0));
    for (Player player : players) {
      player.setPlayerListHeaderFooter(getHeader(player), getFooter(player));
    }
  }
}
