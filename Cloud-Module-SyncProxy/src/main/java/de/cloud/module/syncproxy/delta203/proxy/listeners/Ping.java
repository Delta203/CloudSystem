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

package de.cloud.module.syncproxy.delta203.proxy.listeners;

import de.cloud.module.syncproxy.delta203.proxy.SyncProxy;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ping implements Listener {

  @EventHandler
  public void onPing(ProxyPingEvent e) {
    ServerPing ping = e.getResponse();
    ServerPing.Protocol version = ping.getVersion();
    ServerPing.Players players = ping.getPlayers();
    ping.setDescriptionComponent(
        new TextComponent(SyncProxy.config.getString("serverList.motd").replace("\\n", "\n")));
    version.setName(SyncProxy.config.getString("serverList.version.name"));
    int id = SyncProxy.config.getInt("serverList.version.id");
    if (id != 0) version.setProtocol(id);
    List<ServerPing.PlayerInfo> infos = new ArrayList<>();
    for (String s : SyncProxy.config.getStringList("serverList.description")) {
      infos.add(new ServerPing.PlayerInfo(s, UUID.randomUUID()));
    }
    players.setSample(infos.toArray(new ServerPing.PlayerInfo[0]));
  }
}
