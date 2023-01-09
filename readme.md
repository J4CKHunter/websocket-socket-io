
### websocket example with SocketIO

##### followed source :

```console
https://www.youtube.com/watch?v=wsDeTuuhZRg
```

```console
https://github.com/folksdev/socket-io-basics
```

#### you can use Postman's beta feature of "WebSocket Requests with SocketIO" to test the WebSocket
<pre>
    socket-server.port=9000 -> request url i.e. ws://192.168.1.2:9000?room=math
    client version -> v2
    params -> i.e. room: math, room:english
    events -> get_message: listen on connect
    send -> send_message
    example request body in  JSON format:
        {
            "content": "hello world"
        }
</pre>
