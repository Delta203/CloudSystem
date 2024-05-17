package de.cloud.master.delta203.main.commands;

import de.cloud.master.delta203.main.sockets.Channel;
import de.cloud.master.delta203.main.Cloud;

import java.util.List;

public class Channels {

  public Channels() {}

  public void execute() {
    Cloud.console.print("The list of connected channels:");
    List<Channel> channels = Cloud.server.getChannels();
    if (channels.isEmpty()) {
      Cloud.console.print("There is currently no active channel.");
    }
    for (Channel channel : channels) {
      Cloud.console.print(
          "- "
              + channel.getChannelName()
              + ":"
              + channel.getChannelPort()
              + " ["
              + channel.getChannelState()
              + "]");
    }
  }
}
