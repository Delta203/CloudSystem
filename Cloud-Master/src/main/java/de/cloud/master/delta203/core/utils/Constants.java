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

package de.cloud.master.delta203.core.utils;

import java.util.HashMap;

public class Constants {

  public static class Locals {
    /** The version of the cloud master. */
    public static String VERSION = "1.0.1-SNAPSHOT";

    /** The root directory for the cloud system. */
    public static String ROOT = "cloud/";

    /** The API version for the cloud system. */
    public static String API = "no api";

    /** The default port for minecraft servers. */
    public static int DEFAULT_PORT = 25565;

    /** The starting port for dynamic servers. */
    public static int START_PORT = 40001;

    /** The highest port for dynamic servers. Equivalent to max amount of services. */
    public static int MAX_PORT = 49999;

    /** The start command for Linux systems. */
    public static String START_LINUX =
        "java -Xms%memory%M -Xmx%memory%M -XX:+UseG1GC -DIReallyKnowWhatIAmDoingISwear -jar %file% nogui";

    /** The start command for Windows systems. */
    public static String START_WINDOWS =
        "java -Xms%memory%M -Xmx%memory%M -XX:+UseG1GC -DIReallyKnowWhatIAmDoingISwear -jar %file% nogui";
  }

  public static class Links {
    /** The map of proxy names to their download urls. */
    public static HashMap<String, String> PROXIES =
        new HashMap<>() {
          {
            put(
                "BungeeCord",
                "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar");
            put(
                "Waterfall-1.21",
                "https://api.papermc.io/v2/projects/waterfall/versions/1.21/builds/579/downloads/waterfall-1.21-579.jar");
          }
        };

    /** The map of server names to their download urls. */
    public static HashMap<String, String> SERVERS =
        new HashMap<>() {
          {
            put("spigot-1.18.2", "https://download.getbukkit.org/spigot/spigot-1.18.2.jar");
            put("spigot-1.19", "https://download.getbukkit.org/spigot/spigot-1.19.jar");
            put("spigot-1.19.1", "https://download.getbukkit.org/spigot/spigot-1.19.1.jar");
            put("spigot-1.19.2", "https://download.getbukkit.org/spigot/spigot-1.19.2.jar");
            put("spigot-1.19.3", "https://download.getbukkit.org/spigot/spigot-1.19.3.jar");
            put("spigot-1.19.4", "https://download.getbukkit.org/spigot/spigot-1.19.4.jar");
            put("spigot-1.20.1", "https://download.getbukkit.org/spigot/spigot-1.20.1.jar");
            put("spigot-1.20.2", "https://download.getbukkit.org/spigot/spigot-1.20.2.jar");
            put("spigot-1.20.4", "https://download.getbukkit.org/spigot/spigot-1.20.4.jar");

            put(
                "paper-1.20.1",
                "https://api.papermc.io/v2/projects/paper/versions/1.20.1/builds/196/downloads/paper-1.20.1-196.jar");
            put(
                "paper-1.20.2",
                "https://api.papermc.io/v2/projects/paper/versions/1.20.2/builds/318/downloads/paper-1.20.2-318.jar");
            put(
                "paper-1.20.4",
                "https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/398/downloads/paper-1.20.4-398.jar");
          }
        };
  }
}
