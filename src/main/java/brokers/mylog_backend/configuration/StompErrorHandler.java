package brokers.mylog_backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Configuration
public class StompErrorHandler extends StompSubProtocolErrorHandler {

  private Message<byte[]> errorMessage(Message<byte[]> clientMessage, String errorMessage) {
    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
    String payload = null;
    try {
      payload = new String(clientMessage.getPayload(), "UTF8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    accessor.setMessage(payload);
    accessor.setNativeHeader("errorCode", errorMessage);
    accessor.setLeaveMutable(true);

    return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8),
            accessor.getMessageHeaders());
  }
}