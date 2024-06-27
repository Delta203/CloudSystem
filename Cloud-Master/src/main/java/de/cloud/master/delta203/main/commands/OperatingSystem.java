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

package de.cloud.master.delta203.main.commands;

import com.sun.management.OperatingSystemMXBean;
import de.cloud.master.delta203.main.Cloud;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class OperatingSystem {

  public OperatingSystem() {}

  public void execute() {
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    double cpuLoad = osBean.getCpuLoad() * 100;

    MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
    MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
    long heapUsed = heapUsage.getUsed();
    long heapMax = heapUsage.getMax();
    long nonHeapUsed = nonHeapUsage.getUsed();
    long nonHeapMax = nonHeapUsage.getMax();

    Cloud.console.print("OS: " + Cloud.os);
    Cloud.console.print("CPU Load: " + String.format("%.2f", cpuLoad) + "%");
    Cloud.console.print(
        "Heap Memory: Used: "
            + (heapUsed / (1024 * 1024))
            + " MB, Max: "
            + (heapMax / (1024 * 1024))
            + " MB");
    Cloud.console.print(
        "Non-Heap Memory: Used: "
            + (nonHeapUsed / (1024 * 1024))
            + " MB, Max: "
            + (nonHeapMax / (1024 * 1024))
            + " MB");
  }
}
