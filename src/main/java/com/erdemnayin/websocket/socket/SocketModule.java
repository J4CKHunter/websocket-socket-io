package com.erdemnayin.websocket.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.erdemnayin.websocket.model.CustomMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SocketModule {

    private final String ROOM_URL_PARAM = "room";
    private final String EVENT_SEND_MESSAGE = "send_message";

    private final String EVENT_GET_MESSAGE = "get_message";

    private final SocketIOServer socketIOServer;
    private final Logger logger = LoggerFactory.getLogger(SocketModule.class);

    public SocketModule(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addDisconnectListener(onDisconnected());

        // create an Event Listener to use when someone texts to the room
        // and the message is received from others
        socketIOServer.addEventListener(EVENT_SEND_MESSAGE, CustomMessage.class, onMessageReceived());
    }

    private ConnectListener onConnected(){
        return socketIOClient -> {

            // get RoomName from request parameter
            String roomName = socketIOClient.getHandshakeData().getSingleUrlParam(ROOM_URL_PARAM);
            // and join chatroom
            socketIOClient.joinRoom(roomName);

//            socketIOClient
//                    .getNamespace()
//                    .getRoomOperations(roomName)
//                    .sendEvent("get_message", String.format("SocketID: %s is connected to Room: %s", socketIOClient.getSessionId(), roomName));

            // send GET_MESSAGE EVENT to other clients in the same chatroom
            socketIOClient.getNamespace().getRoomOperations(roomName).getClients().forEach(
                    client -> {
                        if(!client.getSessionId().equals(socketIOClient.getSessionId())){
                            client.sendEvent(EVENT_GET_MESSAGE, String.format("SocketID: %s is connected to Room: %s", socketIOClient.getSessionId(), roomName));
                        }
                    }
            );

            logger.info(String.format("SocketID: %s -> Connected to Room -> %s", socketIOClient.getSessionId().toString(), roomName));
        };
    }

    private DisconnectListener onDisconnected(){
        return socketIOClient -> {

            // get RoomName from request parameter
            String roomName = socketIOClient.getHandshakeData().getSingleUrlParam(ROOM_URL_PARAM);

//            socketIOClient
//                    .getNamespace()
//                    .getRoomOperations(roomName)
//                    .sendEvent("get_message", String.format("SocketID: %s is disconnected from Room: %s", socketIOClient.getSessionId(), roomName));

            // send GET_MESSAGE EVENT to other clients in the same chatroom
            socketIOClient.getNamespace().getRoomOperations(roomName).getClients().forEach(
                    client -> {
                        if(!client.getSessionId().equals(socketIOClient.getSessionId())){
                            client.sendEvent(EVENT_GET_MESSAGE, String.format("SocketID: %s is disconnected from Room: %s", socketIOClient.getSessionId(), roomName));
                        }
                    }
            );

            logger.info(String.format("SocketID: %s -> Disconnected from Room -> %s", socketIOClient.getSessionId().toString(), roomName));
        };
    }

    private DataListener<CustomMessage> onMessageReceived(){
        return (socketIOClient, customMessage, ackRequest) -> {

//            socketIOClient.getNamespace().getBroadcastOperations().sendEvent("get_message", customMessage.getContent());

            // kendimize geri döndermemeliyiz, yukarıdaki kendimize de geri gönderir mesajı çünkü broadcast

//            socketIOClient.getNamespace().getAllClients().forEach(client -> {
//                if(!client.getSessionId().equals(socketIOClient.getSessionId())){
//                    client.sendEvent("get_message", customMessage.getContent());
//                }
//            });

            // room logic eklendikten sonra
            String roomName = socketIOClient.getHandshakeData().getSingleUrlParam(ROOM_URL_PARAM);

            // send GET_MESSAGE EVENT to other clients in the same chatroom
            socketIOClient.getNamespace().getRoomOperations(roomName).getClients().forEach(
                    client -> {
                        if(!client.getSessionId().equals(socketIOClient.getSessionId())){
                            client.sendEvent(EVENT_GET_MESSAGE, customMessage.getContent());
                        }
                    }
            );

            logger.info(String.format("SocketID: %s -> send a message -> %s To room -> %s", socketIOClient.getSessionId().toString(), customMessage.getContent(), roomName));

        };


    }

}
