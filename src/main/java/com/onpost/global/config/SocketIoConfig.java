package com.onpost.global.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.listener.ExceptionListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SocketIoConfig {

    private final SocketIoProperties properties;

    private final ExceptionListener onException = new ExceptionListener() {
        @Override
        public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
        }

        @Override
        public void onDisconnectException(Exception e, SocketIOClient client) {
        }

        @Override
        public void onConnectException(Exception e, SocketIOClient client) {
        }

        @Override
        public void onPingException(Exception e, SocketIOClient client) {

        }

        @Override
        public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
            log.error(e.getMessage());
            ctx.close();

            return false;
        }
    };

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        SocketConfig socketConfig = new SocketConfig();
        config.setTransports(Transport.WEBSOCKET);
        config.setExceptionListener(onException);
        config.setHostname(properties.getHost());
        config.setPort(properties.getPort());
        config.setSocketConfig(socketConfig);
        return new SocketIOServer(config);
    }
}
