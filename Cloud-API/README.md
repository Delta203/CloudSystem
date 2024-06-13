# Cloud-API
All necessary API usages and information about the Cloud-API will be documented in this file.

## Core
### Cloud Channel
The `CloudChannel` connects to the server socket and provides communication with the Cloud-Master. 
The `CloudCommunication` handler handles the messages that are sent between the sockets.

### Packets
The packets represent the messages and act as a message builder, making it easier to convert the 
messages into JSON format and then send them.

Example:
```java
CloudPacketConnect packetConnect = new CloudPacketConnect();    // initialise
packetConnect.k(key);                                           // add key
packetConnect.n(name);                                          // add name
packetConnect.message();
```

## Proxy
### API
```java
/** Get the Cloud-API plugin instance. */
public static CloudAPI plugin;

/** Get the Cloud-API file configuration. */
public static Configuration config;

/** Get the Cloud-API server manager. */
public static CloudServerManager serverManager;

/** Get the Cloud-Service name. */
public static String name;

/** Get the Cloud-Service channel. */
public static CloudChannel channel;

/** Get the Cloud-Service ip address. */
public static String serverIp;
```

### Server Manager
The `CloudServerManager` helps the proxy to manage servers. The `CloudChannel` receives the packet and a 
server is added or removed based on this information.

### Commands
- _/cloud_ Sends the cloud system main information.
- _/l_, _/lobby_, _/hub_ Sends the player to a random lobby (fallback) server.

## Server
### API
```java
/** Get the Cloud-API plugin instance. */
public static CloudAPI plugin;

/** Get the Cloud-API file configuration. */
public static Configuration config;

/** Get the Cloud-Service name. */
public static String name;

/** Get the Cloud-Service channel. */
public static CloudChannel channel;

/** Get the Cloud-Service ip address. */
public static String serverIp;

/** This method gets the cloud service state. */
public static CloudServerState getServiceState() {}

/** This method sets the CloudServerState to INGAME. */
public static void updateServiceState() {}
```

### Only Proxy
`CloudListenerOnlyProxy` ensures that a player cannot join a sub server using an external proxy server. 
However, it is recommended to install a firewall to make the network more secure.

### Commands
- _/updateState_ Set the service state to INGAME.