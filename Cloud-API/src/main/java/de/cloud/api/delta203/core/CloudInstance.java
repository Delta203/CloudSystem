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

package de.cloud.api.delta203.core;

import de.cloud.api.delta203.core.utils.CloudServiceState;

import java.util.HashMap;
import java.util.List;

/** This is the cloud main instance class. */
public class CloudInstance {

  /** Get the Cloud-Service name */
  public static String name;

  /** Get the Cloud-Service ip address. */
  public static String ip;

  /** Get the Cloud-Service port. */
  public static int port;

  /** Get the Cloud-Service key. */
  public static String key;

  /** Get the Cloud-Service channel. */
  public static CloudChannel channel;

  /** Get the Cloud-Service state. */
  public static CloudServiceState state;

  /** Get all Cloud-Services with it states. */
  public static HashMap<CloudServiceState, List<CloudService>> services;

  /** This is an empty main function */
  public static void main(String[] args) {}
}
