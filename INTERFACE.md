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
## API
### CloudPacketConnect:
A socket connects to the server and is then registered by the server.
```json5
"data": {
  "service": String // Service name
}
```
### CloudPacketInGame:
Switch a server to `INGAME` and a new server will be started automatically if the cloud storage is not exhausted.
```json5
"data": {}
```
### Add Server:
The cloud sends this message to the proxy to add a server.
```json5
"data": {
  "name": String,   // Server name
  "ip":   String,   // Server ip
  "port": int       // Server port
}
```
### Remove Server:
The cloud sends this message to the proxy to remove a server.
```json5
"data": {
  "name": String    // Server name
}
```
### Command:
Send a command to a service.
```json5
"data": {
  "command": String // Command
}
```