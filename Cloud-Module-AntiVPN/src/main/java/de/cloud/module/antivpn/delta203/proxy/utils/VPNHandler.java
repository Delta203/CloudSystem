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

package de.cloud.module.antivpn.delta203.proxy.utils;

import de.cloud.module.antivpn.delta203.proxy.AntiVPN;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VPNHandler {

  private final String api;

  private final List<String> allowedAddresses;
  private final List<String> deniedAddresses;

  public VPNHandler() {
    api = AntiVPN.config.getString("api");
    allowedAddresses = new ArrayList<>();
    deniedAddresses = new ArrayList<>();
  }

  public boolean isAllowedAddress(String ip) {
    return allowedAddresses.contains(ip);
  }

  public boolean isDeniedAddress(String ip) {
    return deniedAddresses.contains(ip);
  }

  public boolean fetchAddress(String ip) {
    try {
      URL url = new URL(api.replace("%ip%", ip));
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      InputStreamReader reader = new InputStreamReader(connection.getInputStream());
      Scanner scanner = new Scanner(reader);
      StringBuilder response = new StringBuilder();
      while (scanner.hasNext()) {
        response.append(scanner.next());
      }
      if (response.toString().contains("\"proxy\":true")) {
        // deny address
        deniedAddresses.add(ip);
        return true;
      }
      // allow address
      allowedAddresses.add(ip);
      return false;
    } catch (IOException ignored) {
    }
    // not able to fetch, however allow without caching
    return false;
  }
}
