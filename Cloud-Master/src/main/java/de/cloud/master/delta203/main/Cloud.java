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

package de.cloud.master.delta203.main;

import de.cloud.master.delta203.core.Console;
import de.cloud.master.delta203.core.Group;
import de.cloud.master.delta203.core.Service;
import de.cloud.master.delta203.core.files.FileManager;
import de.cloud.master.delta203.core.files.PathManager;
import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.core.utils.OSType;
import de.cloud.master.delta203.main.sockets.Server;

import java.util.HashMap;
import java.util.List;

public class Cloud {

  public static PathManager pathManager;
  public static FileManager config;

  public static Console console;
  public static Server server;
  public static OSType os;

  public static String key;
  public static int memory = 0;
  public static List<Group> groups;
  public static HashMap<String, Service> services;

  public static boolean shutdown = false;

  public static void main(String[] args) {
    if (args.length == 1) {
      // load cloud without root directory
      if (args[0].equalsIgnoreCase("-NOROOT")) Constants.Locals.ROOT = "";
    }
    pathManager = new PathManager();
    boolean doSetup = pathManager.mkdir();
    config = new FileManager(pathManager.getPathData(), "config.json");
    console = new Console();

    Application application = new Application();
    if (doSetup) application.runSetup();
    application.runMain();
  }
}
