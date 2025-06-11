package es.sujes71.shared.domain.ports.outbound;

import es.sujes71.shared.domain.model.Message;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OutboundPort {

  private static final Map<String, EventHandler<?, ?>> eventBus = new HashMap<>();

  private OutboundPort() {
  }

  public static <B, R> R requestEvent(Message<B> event) {
    EventHandler<B, R> handler = resolve(event.address());
    if (handler == null) {
      throw new IllegalArgumentException("No handler found for address: " + event.address());
    }
    return handler.handle(event.body());
  }

  public static <B, R> void register(String address, EventHandler<B, R> handler) {
    eventBus.put(address, handler);
  }

  @SuppressWarnings("unchecked")
  private static <B, R> EventHandler<B, R> resolve(String address) {
    return (EventHandler<B, R>) eventBus.get(address);
  }

  @FunctionalInterface
  public interface EventHandler<B, R> {
    R handle(B body);
  }
}