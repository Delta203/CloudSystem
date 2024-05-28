package de.cloud.api.delta203.spigot.listeners;

import de.cloud.api.delta203.spigot.CloudAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Login implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onLogin(PlayerLoginEvent e) {
    System.out.println(CloudAPI.serverIp);
    System.out.println(e.getAddress().getHostAddress());
    if (e.getAddress().getHostAddress().equals(CloudAPI.serverIp)
        || e.getAddress().getHostAddress().equals("localhost")
        || e.getAddress().getHostAddress().equals("127.0.0.1")) return;
    e.disallow(
        PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You must join through the proxy.");
  }
}
