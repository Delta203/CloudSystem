# Server - API Communication
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
  CONNECT,          // 0
  INGAME,           // 1
  ADDSERVER,        // 2
  REMOVESERVER,     // 3
  DISCONNECT        // Automatically
}
```
### Connect:
A socket connects to the server and is then registered by the server.
```json5
"data": {
  "name": String,   // Server name
  "port": int       // Server port
}
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
### In game:
Switch a server to `ingame` and a new server will be started automatically if the cloud storage is not exhausted.
```json5
"data": {}
```
### Disconnect:
As soon as a socket is closed, it automatically disconnects from the server. Therefore, the message is not required manually.