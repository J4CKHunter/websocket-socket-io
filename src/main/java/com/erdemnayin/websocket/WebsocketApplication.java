package com.erdemnayin.websocket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketApplication implements CommandLineRunner {

	public WebsocketApplication(SocketIOServer socketIOServer) {
		this.socketIOServer = socketIOServer;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebsocketApplication.class, args);
	}

	private final SocketIOServer socketIOServer;

	@Override
	public void run(String... args) throws Exception {
		socketIOServer.start();
	}
}
