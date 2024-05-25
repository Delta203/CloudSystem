package de.cloud.api.delta203.bungee;

import de.cloud.api.delta203.core.Channel;
import de.cloud.api.delta203.core.utils.ServerState;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class CloudAPI extends Plugin {

  public static CloudAPI plugin;
  public static Configuration config;
  public static List<ServerInfo> fallbacks;

  public static ServerState state;
  public static String name;

  private String serverIp;
  private int serverPort;
  private String serverKey;

  @Override
  public void onEnable() {
    plugin = this;
    fallbacks = new ArrayList<>();
    state = ServerState.LOBBY;
    loadConfig();
    connect();
  }

  private void loadConfig() {
    FileManager configYml = new FileManager("config.yml");
    configYml.create();
    configYml.load();
    config = configYml.get();
    name = config.getString("name");
    serverIp = config.getString("server.ip");
    serverPort = config.getInt("server.port");
    serverKey = config.getString("server.key");
  }

  private void connect() {
    Channel channel = new Channel(serverIp, serverPort, serverKey);
    channel.connect(name, 25565);
  }

  private static void updateFallbacks() {
    for (ListenerInfo info : ProxyServer.getInstance().getConfig().getListeners()) {
      info.getServerPriority().clear();
      for (ServerInfo serverInfo : fallbacks) {
        info.getServerPriority().add(serverInfo.getName());
      }
    }
  }

  public static void addServer(String name, SocketAddress address) {
    ServerInfo serverInfo =
        ProxyServer.getInstance().constructServerInfo(name, address, "Cloud server", false);
    ProxyServer.getInstance().getServers().put(name, serverInfo);
    if (name.toLowerCase().startsWith("lobby")) {
      CloudAPI.fallbacks.add(serverInfo);
      updateFallbacks();
    }
    System.out.println(
        "+ "
            + name
            + (CloudAPI.fallbacks.contains(serverInfo) ? "*" : "")
            + " ("
            + address.toString()
            + ")");
  }

  public static void removeServer(String name) {
    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(name);
    ProxyServer.getInstance().getServers().remove(name);
    if (name.toLowerCase().startsWith("lobby")) {
      CloudAPI.fallbacks.remove(serverInfo);
      updateFallbacks();
    }
    System.out.println("- " + name);
  }
}
