package de.cloud.api.delta203.bungee.commands;

import de.cloud.api.delta203.bungee.CloudAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Random;

public class Lobby extends Command {

  public Lobby(String name) {
    super(name);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (sender instanceof ProxiedPlayer p) {
      if (CloudAPI.fallbacks.contains(p.getServer().getInfo())) {
        p.sendMessage(new TextComponent(ChatColor.RED + "You are already on a lobby server!"));
        return;
      }
      String name =
          CloudAPI.fallbacks.get(new Random().nextInt(CloudAPI.fallbacks.size())).getName();
      ServerInfo server = ProxyServer.getInstance().getServerInfo(name);
      p.connect(server);
    }
  }
}
