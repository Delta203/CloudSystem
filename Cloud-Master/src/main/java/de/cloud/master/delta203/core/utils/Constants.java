package de.cloud.master.delta203.core.utils;

import java.util.HashMap;

public class Constants {

  public static class Locals {
    public static String VERSION = "1.0-SNAPSHOT";
    public static String ROOT = "cloud/";
    public static String API = "no api";

    public static int DEFAULT_PORT = 25565;
    public static int START_PORT = 40001;

    public static String START_LINUX = "Linux start";
    public static String START_WINDOWS =
        "java -Xms%memory%M -Xmx%memory%M -XX:+UseG1GC -DIReallyKnowWhatIAmDoingISwear -jar %file% nogui";
  }

  public static class Links {
    public static HashMap<String, String> PROXIES =
        new HashMap<>() {
          {
            put(
                "BungeeCord",
                "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar");
          }
        };
    public static HashMap<String, String> SERVERS =
        new HashMap<>() {
          {
            put("spigot-1.20.6", "https://download.getbukkit.org/spigot/spigot-1.20.6.jar");
            put("spigot-1.20.1", "https://download.getbukkit.org/spigot/spigot-1.20.1.jar");
            put("spigot-1.18.2", "https://download.getbukkit.org/spigot/spigot-1.18.2.jar");
            put("spigot-1.12.2", "https://cdn.getbukkit.org/spigot/spigot-1.12.2.jar");
            put(
                "spigot-1.8.8",
                "https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar");
          }
        };
  }
}
