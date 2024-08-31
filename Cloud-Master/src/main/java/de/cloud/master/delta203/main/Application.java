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

import de.cloud.master.delta203.core.utils.Constants;
import de.cloud.master.delta203.main.processes.Main;
import de.cloud.master.delta203.main.processes.Setup;
import de.cloud.master.delta203.main.processes.Start;
import java.util.Scanner;

public class Application {

  public static Scanner scanner;

  public Application() {
    scanner = new Scanner(System.in);
    Cloud.console.clear();
  }

  public void runSetup() {
    new Setup().run();
  }

  private void runStart() {
    new Start().run();
  }

  public void runMain() {
    Cloud.console.print("The cloud has started...");
    Cloud.console.print("Running current version: " + Constants.Locals.VERSION);
    runStart();
    new Main().run();
  }
}
