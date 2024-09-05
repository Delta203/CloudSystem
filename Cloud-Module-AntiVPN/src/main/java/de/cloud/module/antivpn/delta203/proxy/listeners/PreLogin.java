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

package de.cloud.module.antivpn.delta203.proxy.listeners;

import de.cloud.module.antivpn.delta203.proxy.AntiVPN;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

public class PreLogin implements Listener {

  @EventHandler
  public void onLogin(PreLoginEvent e) {
    String ip =
        ((InetSocketAddress) e.getConnection().getSocketAddress()).getAddress().getHostAddress();
    if (AntiVPN.vpnHandler.isAllowedAddress(ip)) return;
    if (AntiVPN.vpnHandler.isDeniedAddress(ip)) {
      e.setCancelled(true);
      e.setReason(new TextComponent(ChatColor.RED + "VPN is not allowed on this network. {0}"));
      return;
    }
    if (AntiVPN.vpnHandler.fetchAddress(ip)) {
      e.setCancelled(true);
      e.setReason(new TextComponent(ChatColor.RED + "VPN is not allowed on this network. {-1}"));
    }
  }
}
