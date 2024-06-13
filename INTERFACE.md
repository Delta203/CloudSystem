# Packet Communication
All necessary messages and information between server and sockets will be documented in this file.
## Structure
```json5
{
  "key": String,    // Server key
  "type": String,   // Message type
  "data" {
    ...             // Content          
  }
}
```
## Key Generation
```java
public void generate() {
  String raw = getRandom() + getDate() + getRandom();
  key = sha256(raw);
}
```
## Message Types
```json5
Type {
  CONNECT,          // server
  INGAME,           // server
  ADDSERVER,        // proxy
  REMOVESERVER,     // proxy
  COMMAND           // proxy
}
```
## PacketConnect:
A socket connects to the server and is then registered by the server.
```json5
"type": "CONNECT",
"data": {
  "name": String    // Service name
}
```
## PacketInGame:
Switch a server to `INGAME` and a new server will be started automatically if the cloud storage is not exhausted.
```json5
"type": "INGAME",
"data": {}
```
## PacketAddServer:
The cloud sends this message to the proxy to add a server.
```json5
"type": "ADDSERVER",
"data": {
  "name": String,   // Server name
  "ip":   String,   // Server ip
  "port": int       // Server port
}
```
## PacketRemoveServer
The cloud sends this message to the proxy to remove a server.
```json5
"type": "REMOVESERVER",
"data": {
  "name": String    // Server name
}
```
## PacketCommand:
Send a command to a service.
```json5
"type": "COMMAND",
"data": {
  "command": String // Command
}
```