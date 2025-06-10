package es.sujes71.shared.domain.ports.inbound;

@FunctionalInterface
public interface UseCase<I, R> {

  R execute(I input);
}
