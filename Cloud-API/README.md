# Cloud-API
All necessary API usages and information about the Cloud-API will be documented in this file. <br>
Version: 1.0.1-SNAPSHOT <br>
![](https://github.com/Delta203/CloudSystem/blob/main/.img/icon.png)

## Core
### Cloud Channel
The `CloudChannel` connects to the server socket and provides communication with the Cloud-Master. 
The `CloudCommunication` handler handles the messages that are sent between the sockets.

### Cloud Instance
The `CloudInstance` class contains static service data, which must not be separately saved in the API.
```java
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

/** Get the Cloud-Service state. (Read only!) */
public static CloudServiceState state;

/** Get all Cloud-Services with it states. */
public static HashMap<CloudServiceState, List<CloudService>> services;

/** This is an empty main function */
public static void main(String[] args) {}
```

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

### Services HashMap
The `HashMap<CloudServiceState, List<CloudService>>` contains all services and their state information. It is then updated when:
- A service is started.
- A service is stopped.
- A service changes its state.

## Proxy
### API
```java
/** Get the Cloud-API plugin instance. */
public static CloudAPI plugin;

/** Get the Cloud-API file configuration. */
public static Configuration config;

/** Get the Cloud-API server manager. */
public static CloudServerManager serverManager;
```

### Server Manager
The `CloudServerManager` helps the proxy to manage servers. The `CloudChannel` receives the packet and a 
server is added or removed based on this information.

### Kick Hub
`CloudListenerKickHub` ensures that a player is not kicked directly from the network if a server stops.
A matching fallback server is searched and the player is directed to it when one is found.

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

/** This method sets the CloudServiceState to INGAME. */
public static void updateServiceState() {}
```

### Only Proxy
`CloudListenerOnlyProxy` ensures that a player cannot join a sub server using an external proxy server. 
However, it is recommended to install a firewall to make the network more secure.

### Commands
- _/serviceInfo_ Shows the information of current and all connected services.
- _/updateState_ Set the service state to INGAME.
