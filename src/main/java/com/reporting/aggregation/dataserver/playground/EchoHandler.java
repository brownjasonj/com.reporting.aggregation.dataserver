package com.reporting.aggregation.dataserver.playground;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

public class EchoHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession
                .send(webSocketSession.receive()
                .map(msg -> "Received on Server :: " + msg.getPayloadAsText())
                .map(webSocketSession::textMessage)
                );
    }
}
