package com.github.garodriguezlp.irimo.service;

import com.github.garodriguezlp.irimo.service.exception.ServiceNotFoundException;
import java.util.List;

public class ServiceProvider<T extends Identifiable> {

  private final List<T> services;

  public ServiceProvider(List<T> services) {
    this.services = services;
  }

  public T get(String identity) {
    return services.stream()
        .filter(service -> service.getIdentifier().equalsIgnoreCase(identity))
        .findFirst()
        .orElseThrow(
            () -> new ServiceNotFoundException(identity));
  }
}
