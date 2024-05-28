package de.cloud.api.delta203.spigot;

import de.cloud.api.delta203.core.Channel;
import de.cloud.api.delta203.core.utils.ServerState;
import de.cloud.api.delta203.spigot.listeners.Login;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class CloudAPI extends JavaPlugin {

  public static CloudAPI plugin;

  public static ServerState state;
  public static String name;

  public static String serverIp;
  private int serverPort;
  private String serverKey;

  @Override
  public void onEnable() {
    plugin = this;
    state = ServerState.LOBBY;
    loadConfig();
    connect();

    Bukkit.getPluginManager().registerEvents(new Login(), plugin);
  }

  private void loadConfig() {
    FileManager configYml = new FileManager("config.yml");
    configYml.create();
    configYml.load();
    Configuration config = configYml.get();
    name = config.getString("name");
    serverIp = config.getString("server.ip");
    serverPort = config.getInt("server.port");
    serverKey = config.getString("server.key");
  }

  private void connect() {
    Channel channel = new Channel(serverIp, serverPort, serverKey);
    channel.connect(name);
  }
}
